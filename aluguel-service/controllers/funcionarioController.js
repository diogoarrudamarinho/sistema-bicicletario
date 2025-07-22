const express = require("express");
const router = express.Router();
const funcionarioServices = require('../services/funcionario');

router.get('/funcionarios', async (req, res) => {
    try {
        const funcionarios = await funcionarioServices.retornaTodosFuncionarios();
        res.status(200).json(funcionarios);
    } catch (error) {
        if (error.response) {
            return res.status(error.response.status).json({
                erro: error.response.data?.mensagem || error.response.data || 'Erro na API externa'
            });
        }
        res.status(500).json({ erro: error.message || 'Erro interno do servidor' });
    }
});

router.get('/funcionario/:id', async (req, res) => {
    const id = req.params.id;
    if (!id) {
        return res.status(404).json({ erro: 'ID não fornecido' });
    }

    try {
        const funcionario = await funcionarioServices.buscaFuncionarioPorId(id);
        if (!funcionario) {
            return res.status(404).json({ erro: 'Funcionário não encontrado' });
        }
        res.status(200).json(funcionario);
    } catch (error) {
        if (error.response) {
            return res.status(error.response.status).json({
                erro: error.response.data?.mensagem || error.response.data || 'Erro na API externa'
            });
        }
        res.status(500).json({ erro: error.message || 'Erro interno do servidor' });
    }
});

router.put('/funcionario/:id', async (req, res) => {
    const id = req.params.id;
    const novosDados = req.body;

    if (!id || !novosDados) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }

    try {
        const funcionarioAtualizado = await funcionarioServices.atualizaFuncionario(id, novosDados);
        res.status(200).json(funcionarioAtualizado);
    } catch (error) {
        if (error.response) {
            return res.status(error.response.status).json({
                erro: error.response.data?.mensagem || error.response.data || 'Erro na API externa'
            });
        }
        res.status(500).json({ erro: error.message || 'Erro interno do servidor' });
    }
});

router.post('/funcionario', async (req, res) => {
    const funcionario = req.body;

    if (!funcionario?.nome || !funcionario?.email) {
        return res.status(400).json({ erro: 'Requisição mal formada' });
    }

    try {
        const novoFuncionario = await funcionarioServices.criaFuncionario(funcionario);
        res.status(201).json(novoFuncionario);
    }catch (error) {
        if (error.response) {
            return res.status(error.response.status).json({
                erro: error.response.data?.mensagem || error.response.data || 'Erro na API externa'
            });
        }
        res.status(500).json({ erro: error.message || 'Erro interno do servidor' });
    }
});

router.delete('/funcionario/:id', async (req, res) => {
    const id = req.params.id;

    if (!id) {
        return res.status(404).json({ erro: 'ID não fornecido' });
    }

    try {
        const resultado = await funcionarioServices.deletaFuncionario(id);
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