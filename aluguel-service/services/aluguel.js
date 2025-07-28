const database = require('../repositories/acessoDB/aluguelDB');
const ciclistaDB = require("../repositories/acessoDB/ciclistaDB");
const axios = require('axios');
const URL_EXTERNO = 'http://externo:8080';
const URL_EQUIPAMENTO = 'http://equipamento:8080'


async function alugarBicicleta(idCiclista, idTranca) {

    let bicicleta = await axios.get(`${URL_EQUIPAMENTO}/tranca/${idTranca}/bicicleta`)

    if (bicicleta.data?.status === "DISPONIVEL") {
        let aluguelAtivo = await database.verificaAluguelAtivo(idCiclista);
        if (!aluguelAtivo) {
            let dataInicio = new Date().toISOString();

            const cobranca = {
                valor: 10.0,
                idCiclista: idCiclista
            };

            await axios.post(`${URL_EQUIPAMENTO}/bicicleta/${bicicleta.data.id}/status/EM_USO`);
            await axios.post(`${URL_EQUIPAMENTO}/tranca/${idTranca}/destrancar`);
            
            await axios.post(`${URL_EXTERNO}/cobranca`, cobranca);

            const ciclista = await ciclistaDB.recuperaCiclista(idCiclista);

            const emailPayload = {
                    destinatario: ciclista.email,
                    assunto: 'Aluguel feito com sucesso - Sistema Bicicletário',
                    mensagem: `Link para visualizar o aluguel: http://localhost:8082/aluguel/${ciclista.id}\nValor: R$10`
                };

                await axios.post(`${URL_EXTERNO}/enviarEmail`, emailPayload, {
                    headers: { 'Content-Type': 'application/json' }
                });

            return await database.registraAluguel(idCiclista, bicicleta.id, dataInicio);

        } else {
            throw new Error('Já possui um aluguel ativo. Finalizar o aluguel atual antes de alugar uma nova bicicleta.');
        }
    } else {
        throw new Error('Bicicleta não disponível para aluguel');
    }
}

async function devolverBicicleta(trancaFim, idBicicleta) {

    let bicicleta = await axios.get(`${URL_EQUIPAMENTO}/bicicleta/${idBicicleta}`);

    const idCiclista = await database.verificaCiclistaComBicicleta(idBicicleta);
    if (!idCiclista) {
        throw new Error('Nenhum aluguel ativo encontrado para essa bicicleta');
    }

    // Recupera o aluguel ativo para ter acesso à horaInicio
    const aluguelAtivo = await database.recuperarAluguelAtivoPorCiclista(idCiclista);
    if (!aluguelAtivo?.horaInicio) {
        throw new Error('Aluguel ativo não encontrado ou incompleto');
    }

    // Define horaFim como agora
    const horaFim = new Date().toISOString();

    // Finaliza o aluguel
    const NovoAluguel = await database.finalizarAluguel({ ciclista: idCiclista }, trancaFim, horaFim);

    // Calcula tempo total de uso em minutos
    const inicio = new Date(aluguelAtivo.horaInicio);
    const fim = new Date(horaFim);
    const minutos = Math.ceil((fim - inicio) / (1000 * 60));

    // Regra de cobrança: R$5 a cada 30min acima de 2h
    let valorAdicional = 0;
    if (minutos > 120) {
        const minutosExcedentes = minutos - 120;
        const blocosDe30min = Math.ceil(minutosExcedentes / 30);
        valorAdicional = blocosDe30min * 5;
    }

    // Atualiza status da bicicleta
    await axios.post(`${URL_EQUIPAMENTO}/bicicleta/${bicicleta.data?.id}/status/DISPONIVEL`);
    await axios.post(`${URL_EQUIPAMENTO}/tranca/${trancaFim}/trancar`, { idBicicleta: bicicleta.data?.id },{
                    headers: { 'Content-Type': 'application/json' }
                });
    
    // Recupera dados do ciclista
    const ciclista = await ciclistaDB.recuperaCiclista(idCiclista);

    // Envia e-mail
    const emailPayload = {
        destinatario: ciclista.email,
        assunto: 'Devolução de Aluguel - Sistema Bicicletário',
        mensagem: 'Bicicleta devolvida com sucesso.\n' +
                  `Tempo total de uso: ${minutos} minutos.\n` +
                  (valorAdicional > 0
                      ? `Valor adicional cobrado: R$${valorAdicional},00.`
                      : `Nenhuma cobrança adicional aplicada.`) +
                  `\nAcesse o aluguel: http://localhost:8082/aluguel/${ciclista.id}`
    };

    await axios.post(`${URL_EXTERNO}/enviarEmail`, emailPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    const cobranca = {
        valor: valorAdicional,
        idCiclista: idCiclista
    };

    await axios.post(`${URL_EXTERNO}/cobranca`, cobranca);

    // Retorna a resposta
    return {
        mensagem: 'Bicicleta devolvida com sucesso',
        aluguel: NovoAluguel,
        valorAdicional: valorAdicional,
        minutos: minutos
    }
}

module.exports = {
    alugarBicicleta,
    devolverBicicleta
};