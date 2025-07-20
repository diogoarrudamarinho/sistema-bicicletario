const fs = require('fs');
const path = require('path');
const axios = require('axios');
const dbPath = path.join(__dirname, '../repositories/ciclistas.json');
const URL_EXTERNO = 'http://externo:8080/externo';


async function recuperaCartao(idCiclista){
    let db;
    try {
        const raw = fs.readFileSync(dbPath, 'utf-8');
        db = JSON.parse(raw);
    } catch (error) {
        throw new Error('Erro ao ler o banco de dados:' + error.message);
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
        throw new Error('Erro ao ler o banco de dados' + error.message);
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

    await axios.post(`${URL_EXTERNO}/cartao/validar`, db.meiosPagamento[cartaoIndex]);

    // 4. Salva o banco de volta no arquivo
    try {
        fs.writeFileSync(dbPath, JSON.stringify(db, null, 2), 'utf-8');
    } catch (error) {
        throw new Error('Erro ao salvar no banco de dados ' + error.message);
    }

    const ciclista = db.ciclistas.find(c => c.id === idCiclista);

    const emailPayload = {
        destinatario: ciclista.email,
        assunto: 'Alteração de Dados - Sistema Bicicletário',
        mensagem: 'Dados Alterados com sucesso. Se não foi você não é problema nosso, boa sorte!'
    };

    await axios.post(`${URL_EXTERNO}/email/enviarEmail`, emailPayload, {
            headers: { 'Content-Type': 'application/json' }
    });

    return db.meiosPagamento[cartaoIndex];
}

module.exports = {
    recuperaCartao,
    alterarCartao
}