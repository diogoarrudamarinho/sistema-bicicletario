const express = require('express');
const router = express.Router();
const { limparBaseDeDados } = require('../services/admin');

router.get('/restaurarBanco', async (req, res) => {
  try {
    await limparBaseDeDados();
    res.status(200).json({ mensagem: 'Base de dados limpa com sucesso' });
  } catch (error) {
    res.status(500).json({ erro: 'Erro ao limpar base de dados', detalhes: error.message });
  }
});

module.exports = router;