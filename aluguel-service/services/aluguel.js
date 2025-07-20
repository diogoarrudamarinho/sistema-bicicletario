const database = require('../repositories/acessoDB/aluguelDB');
const ciclistaDB = require("../repositories/acessoDB/ciclistaDB");
const axios = require('axios');
const URL_EXTERNO = 'http://externo:8080/externo';


async function alugarBicicleta(idCiclista, idTranca) {
    let bicicleta = await database.recuperaBicicletaPorTranca(idTranca);

    if (bicicleta.status === "disponível") {
        let aluguelAtivo = await database.verificaAluguelAtivo(idCiclista);
        if (!aluguelAtivo) {
            let dataInicio = new Date().toISOString();

            const cobranca = {
                valor: 10.0,
                ciclista: idCiclista
            };
            await axios.post(`${URL_EXTERNO}/cobrancas/cobrar`, cobranca);

            const ciclista = await ciclistaDB.recuperaCiclista(idCiclista);

 const emailPayload = {
        destinatario: ciclista.email,
        assunto: 'Aluguel feito com sucesso - Sistema Bicicletário',
        mensagem: `Link para visualizar o aluguel: http://localhost:8082/aluguel/${ciclista.id}\nValor: R$10`
    };

    await axios.post(`${URL_EXTERNO}/email/enviarEmail`, emailPayload, {
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


     const ciclista = await ciclistaDB.recuperaCiclista(idCiclista);

 const emailPayload = {
        destinatario: ciclista.email,
        assunto: 'Devolução de Aluguel - Sistema Bicicletário',
        mensagem: `Link para visualizar o aluguel: http://localhost:8082/aluguel/${ciclista.id}`
    };

    await axios.post(`${URL_EXTERNO}/email/enviarEmail`, emailPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    return { mensagem: 'Bicicleta devolvida com sucesso' };
}

module.exports = {
    alugarBicicleta,
    devolverBicicleta
};