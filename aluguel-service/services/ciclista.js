const fs = require('fs');
const path = require('path');
let validacao = require('./validacao');
const ciclistaDB = require("../repositories/acessoDB/ciclistaDB");

async function cadastrarCiclista(ciclista, meioDePagamento) {
    try {
        validacao.validarEmail(ciclista.email);
        validacao.validarCPF(ciclista.cpf);
        // Verifica se o email já existe
        const emailExistente = await ciclistaDB.existeEmail(ciclista.email);
        if (emailExistente) {
            throw new Error('Email já cadastrado');
        }

        // Cria o ciclista
        const novoCiclista = await ciclistaDB.criarCiclista(ciclista, meioDePagamento);
        return novoCiclista;
    } catch (error) {
        throw new Error('Erro ao cadastrar ciclista: ' + error.message);
    }
}

async function alteraCiclista(idCiclista){
    try {
        const ciclistaAtualizado = await ciclistaDB.atualizarCiclista(idCiclista);
        return ciclistaAtualizado;
    } catch (error) {
        throw new Error('Erro ao atualizar ciclista: ' + error.message);
    }
}

async function recuperaCiclista(idCiclista){
    try {
        const ciclista = await ciclistaDB.obterCiclista(idCiclista);
        return ciclista;
    } catch (error) {
        throw new Error('Erro ao recuperar ciclista: ' + error.message);
    }
}

async function ativarCiclista(idCiclista) {
    try {
        const ciclistaAtivado = await ciclistaDB.ativarCiclista(idCiclista);
        return ciclistaAtivado;
    } catch (error) {
        throw new Error('Erro ao ativar ciclista: ' + error.message);
    }
}

async function existeEmail(email){
    try {
        const existe = await ciclistaDB.existeEmail(email);
        return existe;
    } catch (error) {
        throw new Error('Erro ao verificar email: ' + error.message);
    }
}


async function removeCiclista(idCiclista) {
    try {
        const resultado = await ciclistaDB.deletarCiclista(idCiclista);
        return resultado;
    } catch (error) {
        throw new Error('Erro ao remover ciclista: ' + error.message);
    }
}


module.exports = {
  createCiclista: cadastrarCiclista,
  updateCiclista: alteraCiclista,
  getCiclistaById: recuperaCiclista,
  activateCiclista: ativarCiclista,
  emailExists: existeEmail,
    removeCiclista
};
