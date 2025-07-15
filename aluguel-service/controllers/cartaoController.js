const express = require("express");
const router = express.Router();
const cartaoMetodos = require('../services/cartao');

router.get('/cartaoDeCredito/:idCiclista', async (req, res) => {
    let idCiclista = req.params.idCiclista;
    if (!idCiclista) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }
    try {
        const cartao = await cartaoMetodos.recuperaCartao(idCiclista);
        res.status(200).json(cartao);
    } catch (error) {
        console.error('Erro ao recuperar cartão de crédito:', error);
        res.status(500).json({ erro: 'Erro interno do servidor' });
    }

});

router.put('/cartaoDeCredito/:idCiclista', async (req, res) => {
    let dados = req.body;
    let idCiclista = req.params.idCiclista;
    if (!dados || !idCiclista) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }

    try {
        const cartaoAtualizado = await cartaoMetodos.alterarCartao(idCiclista, dados);
        res.status(200).json(cartaoAtualizado);
    } catch (error) {
        console.error('Erro ao atualizar cartão de crédito:', error);
        res.status(500).json({ erro: 'Erro interno do servidor' });
    }
});

module.exports = router;