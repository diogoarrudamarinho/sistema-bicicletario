const express = require("express");
const router = express.Router();
const aluguelServices = require('../services/aluguel');

router.post("/aluguel", async (req, res) => {
    const { idCiclista, idTranca } = req.body;

    if (!idCiclista || !idTranca) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }

    try {
        const aluguel = await aluguelServices.alugarBicicleta(idCiclista, idTranca);
        res.status(201).json(aluguel);
    }catch (error) {
        if (error.response) {
            return res.status(error.response.status).json({
                erro: error.response.data?.mensagem || error.response.data || 'Erro na API externa'
            });
        }
        res.status(500).json({ erro: error.message || 'Erro interno do servidor' });
    }
});

router.post('/devolucao', async (req, res) => {
   const { idTranca, idBicicleta } = req.body;

    // Verifica se veio tudo
    if (!idTranca || !idBicicleta) {
         return res.status(404).json({ erro: 'Requisição mal formada' });
    }
    try {
         const resultado = await aluguelServices.devolverBicicleta(idTranca, idBicicleta);
         res.status(200).json(resultado);
    } catch (error) {
        if (error.response) {
            return res.status(error.response.status).json({
                erro: error.response.data?.mensagem || error.response.data || 'Erro na API externa'
            });
        }
        res.status(500).json({ erro: error.message || 'Erro interno do servidor' });
    }
});


module.exports = router;