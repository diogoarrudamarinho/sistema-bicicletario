const fs = require('fs/promises');
const path = require('path');

const dbPath = path.join(__dirname, '../ciclistas.json');

// Função utilitária para ler o banco de dados
async function lerDB() {
    try {
        const raw = await fs.readFile(dbPath, 'utf-8');
        return JSON.parse(raw);
    } catch (error) {
        throw new Error('Erro ao ler o banco de dados: ' + error.message);
    }
}

// Função utilitária para salvar o banco de dados
async function salvarDB(db) {
    try {
        await fs.writeFile(dbPath, JSON.stringify(db, null, 2), 'utf-8');
    } catch (error) {
        throw new Error('Erro ao salvar no banco de dados: ' + error.message);
    }
}

const crypto = require('crypto');

function gerarIdSeguroNum() {
  const buffer = crypto.randomBytes(4);
  const num = buffer.readUInt32BE(0);
  return (10000 + (num % 90000)).toString();
}

async function criarCiclista(ciclista, meioDePagamento) {
    const db = await lerDB();
    const emailExistente = db.ciclistas.find(c => c.email === ciclista.email);
    if (emailExistente) throw new Error('Email já cadastrado');

    const id = gerarIdSeguroNum();
    const pagamentoId = gerarIdSeguroNum();

    const novoCiclista = { id, ativado: false, ...ciclista };
    const novoPagamento = { id: pagamentoId, ciclistaId: id, ...meioDePagamento };

    db.ciclistas.push(novoCiclista);
    db.meiosPagamento.push(novoPagamento);

    await salvarDB(db);
    return novoCiclista;
}

async function atualizarCiclista(id, dados) {
    const db = await lerDB();
    const ciclista = db.ciclistas.find(c => c.id === id);
    if (!ciclista) throw new Error('Ciclista não encontrado');

    Object.entries(dados).forEach(([key, val]) => {
        if (val !== undefined && val !== null && val !== '') {
            if (typeof val === 'object' && !Array.isArray(val)) {
                ciclista[key] = { ...ciclista[key], ...val };
            } else {
                ciclista[key] = val;
            }
        }
    });

    await salvarDB(db);
    return ciclista;
}

async function obterCiclista(id) {
    const db = await lerDB();
    const ciclista = db.ciclistas.find(c => c.id === id);
    if (!ciclista) throw new Error('Ciclista não encontrado');

    const meioDePagamento = db.meiosPagamento.find(m => m.ciclistaId === id) || null;
    return { ...ciclista, meioDePagamento };
}

async function deletarCiclista(id) {
    const db = await lerDB();
    const index = db.ciclistas.findIndex(c => c.id === id);
    if (index === -1) throw new Error('Ciclista não encontrado');

    db.ciclistas.splice(index, 1);
    db.meiosPagamento = db.meiosPagamento.filter(mp => mp.ciclistaId !== id);

    await salvarDB(db);
    return { mensagem: 'Ciclista removido com sucesso' };
}

async function ativarCiclista(id) {
    const db = await lerDB();
    const ciclista = db.ciclistas.find(c => c.id === id);
    if (!ciclista) throw new Error('Ciclista não encontrado');

    ciclista.ativado = true;
    await salvarDB(db);
    return ciclista;
}

async function existeEmail(email) {
    const db = await lerDB();
    return db.ciclistas.some(c => c.email === email);
}

module.exports = {
    criarCiclista,
    atualizarCiclista,
    obterCiclista,
    deletarCiclista,
    ativarCiclista,
    existeEmail
};
