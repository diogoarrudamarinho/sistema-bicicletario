const fs = require('fs/promises');
const path = require('path');

const dbCiclistas = path.join(__dirname, '../ciclistas.json');
const dbBicicletas = path.join(__dirname, '../bicicletas.json');
const dbTrancas = path.join(__dirname, '../trancas.json');
const dbAlugueis = path.join(__dirname, '../alugueis.json');

async function recuperaBicicletaPorTranca(idTranca) {
    try {
        // Lê trancas
        const rawTrancas = await fs.readFile(dbTrancas, 'utf-8'); // precisa do encoding
        const trancas = JSON.parse(rawTrancas).trancas;

        const tranca = trancas.find(t => t.id === idTranca);
        if (!tranca) {
            throw new Error('Tranca não encontrada');
        }

        if (!tranca.bicicleta) {
            throw new Error('Nenhuma bicicleta associada a esta tranca');
        }

        // Lê bicicletas
        const rawBikes = await fs.readFile(dbBicicletas, 'utf-8');
        const bicicletas = JSON.parse(rawBikes).bicicletas;

        const bicicleta = bicicletas.find(b => b.id === tranca.bicicleta && b.status === 'disponível');

        if (!bicicleta) {
            throw new Error('Bicicleta não encontrada ou não disponível');
        }

        return bicicleta;
    } catch (error) {
        throw new Error('Erro ao recuperar bicicleta: ' + error.message);
    }
}

//Verifica se o ciclista já possui um aluguel ativo

async function verificaAluguelAtivo(idCiclista) {
    try {
        const raw = await fs.readFile(dbAlugueis, 'utf-8');
        const alugueis = JSON.parse(raw).alugueis;

        // Aluguel ativo: não tem horaFim (aluguel ainda em andamento)
        const aluguelAtivo = alugueis.find(aluguel =>
            String(aluguel.ciclista) === String(idCiclista) && !aluguel.horaFim
        );

        return aluguelAtivo || false;
    } catch (error) {
        throw new Error('Erro ao verificar aluguel ativo: ' + error.message);
    }
}

//verifica qual ciclista está com a bicicleta
async function verificaCiclistaComBicicleta(idBicicleta) {
    try {
        const rawAlugueis = await fs.readFile(dbAlugueis, 'utf-8');
        const alugueis = JSON.parse(rawAlugueis).alugueis;

        const aluguelAtivo = alugueis.find(aluguel =>
            String(aluguel.bicicleta) === String(idBicicleta) && !aluguel.horaFim
        );

        return aluguelAtivo ? aluguelAtivo.ciclista : null;
    } catch (error) {
        throw new Error('Erro ao verificar ciclista com bicicleta: ' + error.message);
    }
}

// Registra o aluguel da bicicleta no banco de dados
//Atualiza o status da bicicleta para "em uso"
// chama o tranca destrancar -> equipamentos
async function registraAluguel(idCiclista, idBicicleta, dataInicio) {
    try {
        // 1. Carrega trancas
        const rawTrancas = await fs.readFile(dbTrancas, 'utf-8');
        const trancas = JSON.parse(rawTrancas).trancas;

        const tranca = trancas.find(t => t.bicicleta === idBicicleta);
        if (!tranca) throw new Error('Tranca com essa bicicleta não encontrada');

        const idTranca = tranca.id;

        // 2. Atualiza status da bicicleta para "em uso"
        const rawBikes = await fs.readFile(dbBicicletas, 'utf-8');
        const bicicletasJson = JSON.parse(rawBikes);

        const bicicleta = bicicletasJson.bicicletas.find(b => b.id === idBicicleta);
        if (!bicicleta) throw new Error('Bicicleta não encontrada');

        bicicleta.status = 'em uso';

        await fs.writeFile(dbBicicletas, JSON.stringify(bicicletasJson, null, 2));

        // 3. Chama função para destrancar -> equipamentos: destrancar

        // 4. Registra aluguel
        const rawAlugueis = await fs.readFile(dbAlugueis, 'utf-8');
        const alugueisJson = JSON.parse(rawAlugueis);

        const novoAluguel = {
            bicicleta: idBicicleta,
            horaInicio: dataInicio,
            trancaInicio: idTranca,
            horaFim: null,
            trancaFim: null,
            cobranca: null,
            ciclista: idCiclista
        };

        alugueisJson.alugueis.push(novoAluguel);
        await fs.writeFile(dbAlugueis, JSON.stringify(alugueisJson, null, 2));

        return novoAluguel;

    } catch (err) {
        throw new Error('Erro ao registrar aluguel: ' + err.message);
    }
}

async function finalizarAluguel(aluguel, trancaFim, horaFim){
    //pega o aluguel no json que está com os dados do aluguel passado
    //coloca a data fim e a tranca fim
    try {
        const rawAlugueis = await fs.readFile(dbAlugueis, 'utf-8');
        const alugueisJson = JSON.parse(rawAlugueis);

        const aluguelIndex = alugueisJson.alugueis.findIndex(a => a.ciclista === aluguel.ciclista && !a.horaFim);
        if (aluguelIndex === -1) throw new Error('Aluguel não encontrado ou já finalizado');

        // Atualiza o aluguel
        alugueisJson.alugueis[aluguelIndex].horaFim = horaFim;
        alugueisJson.alugueis[aluguelIndex].trancaFim = trancaFim;

        await fs.writeFile(dbAlugueis, JSON.stringify(alugueisJson, null, 2));

        return alugueisJson.alugueis[aluguelIndex];
    } catch (error) {
        throw new Error('Erro ao finalizar aluguel: ' + error.message);
    }
}

async function atualizarStatusBicicleta(idBicicleta, status) {
    try {
        const rawBikes = await fs.readFile(dbBicicletas, 'utf-8');
        const bicicletasJson = JSON.parse(rawBikes);

        const bicicleta = bicicletasJson.bicicletas.find(b => b.id === idBicicleta);
        if (!bicicleta) throw new Error('Bicicleta não encontrada');

        bicicleta.status = status;

        await fs.writeFile(dbBicicletas, JSON.stringify(bicicletasJson, null, 2));
    } catch (error) {
        throw new Error('Erro ao atualizar status da bicicleta: ' + error.message);
    }
}

module.exports = {
    recuperaBicicletaPorTranca,
    verificaAluguelAtivo,
    registraAluguel,
    finalizarAluguel,
    atualizarStatusBicicleta,
    verificaCiclistaComBicicleta,
}
