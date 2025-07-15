const express = require("express");
const router = express.Router();
const funcionarioServices = require('../services/funcionario');

router.get('/funcionarios', async (req, res) => {
    try {
        const funcionarios = await funcionarioServices.retornaTodosFuncionarios();
        res.status(200).json(funcionarios);
    } catch (error) {
        res.status(500).json({ message: `Erro interno do servidor: ${error.message}` });
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
        res.status(500).json({ message: `Erro interno do servidor: ${error.message}` });
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
        res.status(500).json({ message: `Erro interno do servidor: ${error.message}` });
    }
});

router.post('/funcionario', async (req, res) => {
    const funcionario = req.body;

    if (!funcionario || !funcionario.nome || !funcionario.email) {
        return res.status(400).json({ erro: 'Requisição mal formada' });
    }

    try {
        const novoFuncionario = await funcionarioServices.criaFuncionario(funcionario);
        res.status(201).json(novoFuncionario);
    } catch (error) {
        res.status(500).json({ message: `Erro interno do servidor: ${error.message}` });
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
        res.status(500).json({ message: `Erro interno do servidor: ${error.message}` });
    }
});

module.exports = router;