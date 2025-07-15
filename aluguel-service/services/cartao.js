const fs = require('fs');
const path = require('path');
const dbPath = path.join(__dirname, '../repositories/ciclistas.json');
async function recuperaCartao(idCiclista){
    let db;
    try {
        const raw = fs.readFileSync(dbPath, 'utf-8');
        db = JSON.parse(raw);
    } catch (error) {
        throw new Error('Erro ao ler o banco de dados');
    }

    const cartao = db.meiosPagamento.find(m => m.ciclistaId === idCiclista);
    if (!cartao) {
        throw new Error('Cartão não encontrado');
    }
    return cartao;
}

async function alterarCartao(idCiclista, novosDados) {
    let db;

    // 1. Carrega o banco de dados
    try {
        const raw = fs.readFileSync(dbPath, 'utf-8');
        db = JSON.parse(raw);
    } catch (error) {
        throw new Error('Erro ao ler o banco de dados');
    }

    // 2. Encontra o meio de pagamento do ciclista
    const cartaoIndex = db.meiosPagamento.findIndex(m => m.ciclistaId === idCiclista);
    if (cartaoIndex === -1) {
        throw new Error('Cartão não encontrado para o ciclista');
    }

    // 3. Altera apenas os campos fornecidos (mantém os antigos)
    db.meiosPagamento[cartaoIndex] = {
        ...db.meiosPagamento[cartaoIndex],
        ...novosDados
    };

    // 4. Salva o banco de volta no arquivo
    try {
        fs.writeFileSync(dbPath, JSON.stringify(db, null, 2), 'utf-8');
    } catch (error) {
        throw new Error('Erro ao salvar no banco de dados');
    }

    return db.meiosPagamento[cartaoIndex];
}

module.exports = {
    recuperaCartao,
    alterarCartao
}
