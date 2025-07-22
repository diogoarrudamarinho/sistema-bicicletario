const fs = require('fs');
const path = require('path');
const axios = require('axios');
let validacao = require('./validacao');
const ciclistaDB = require("../repositories/acessoDB/ciclistaDB");
const URL_EXTERNO = 'http://externo:8080/';


async function cadastrarCiclista(ciclista, meioDePagamento) {

    await axios.post(`${URL_EXTERNO}/cartao/validar`, meioDePagamento);

    validacao.validarEmail(ciclista.email);
    validacao.validarCPF(ciclista.cpf);
    // Verifica se o email já existe
    const emailExistente = await ciclistaDB.existeEmail(ciclista.email);
    if (emailExistente) {
        throw new Error('Email já cadastrado');
    }

    // Cria o ciclista
    const novoCiclista = await ciclistaDB.criarCiclista(ciclista, meioDePagamento);

    const emailPayload = {
        destinatario: ciclista.email,
        assunto: 'Confirmação de Cadastro - Sistema Bicicletário',
        mensagem: `Link para ativar o cadastro: http://localhost:8082/ciclista/ciclista/${novoCiclista.id}/ativar`
    };

    await axios.post(`${URL_EXTERNO}/enviarEmail`, emailPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    return novoCiclista;

}

async function alteraCiclista(idCiclista) {
    
    const ciclistaAtualizado = await ciclistaDB.atualizarCiclista(idCiclista);
    const emailPayload = {
        destinatario: ciclistaAtualizado.email,
        assunto: 'Alteração de Dados - Sistema Bicicletário',
        mensagem: 'Dados Alterados com sucesso. Se não foi você não é problema nosso, boa sorte!'
    };

    await axios.post(`${URL_EXTERNO}/enviarEmail`, emailPayload, {
        headers: { 'Content-Type': 'application/json' }
    });
    return ciclistaAtualizado;
}

async function recuperaCiclista(idCiclista) {
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

async function existeEmail(email) {
    try {
        const existe = await ciclistaDB.existeEmail(email);
        return existe;
    } catch (error) {
        throw new Error('Erro ao verificar email: ' + error.message);
    }
}


async function removeCiclista(idCiclista) {
    const resultado = await ciclistaDB.deletarCiclista(idCiclista);
    const emailPayload = {
        destinatario: resultado.email,
        assunto: 'Deleção de Dados - Sistema Bicicletário',
        mensagem: 'Dados deletados com sucesso. Se não foi você não é problema nosso, boa sorte!'
    };

    await axios.post(`${URL_EXTERNO}/enviarEmail`, emailPayload, {
        headers: { 'Content-Type': 'application/json' }
    });
        return resultado;

}

module.exports = {
    createCiclista: cadastrarCiclista,
    updateCiclista: alteraCiclista,
    getCiclistaById: recuperaCiclista,
    activateCiclista: ativarCiclista,
    emailExists: existeEmail,
    removeCiclista
};