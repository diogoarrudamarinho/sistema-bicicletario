const database = require('../repositories/acessoDB/aluguelDB');

async function alugarBicicleta(idCiclista, idTranca) {
    let bicicleta = await database.recuperaBicicletaPorTranca(idTranca);

    if (bicicleta.status === "disponível") {
        let aluguelAtivo = await database.verificaAluguelAtivo(idCiclista);
        if (!aluguelAtivo) {
            let dataInicio = new Date().toISOString();
            await database.registraAluguel(idCiclista, bicicleta.id, dataInicio);
        } else {
            throw new Error('Já possui um aluguel ativo. Finalizar o aluguel atual antes de alugar uma nova bicicleta.');
        }
    } else {
        throw new Error('Bicicleta não disponível para aluguel');
    }
}

async function devolverBicicleta(trancaFim, idBicicleta) {
    let idCiclista = await database.verificaCiclistaComBicicleta(idBicicleta);
    if (!idCiclista) {
        throw new Error('Nenhum aluguel ativo encontrado para essa bicicleta');
    }

    const aluguel = {
        ciclista: idCiclista
    };

    let dataFim = new Date().toISOString();
    await database.finalizarAluguel(aluguel, trancaFim, dataFim);

    await database.atualizarStatusBicicleta(idBicicleta, 'disponível');

    return { mensagem: 'Bicicleta devolvida com sucesso' };
}

module.exports = {
    alugarBicicleta,
    devolverBicicleta
};
