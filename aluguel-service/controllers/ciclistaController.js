const express = require("express");
const router = express.Router();
const ciclistaMetodos = require('../services/ciclista');

router.post('/ciclista', async (req, res) => {
    const { ciclista, meioDePagamento, senha, confSenha } = req.body;

    // Verifica se veio tudo
    if (!ciclista || !meioDePagamento || !senha || !confSenha) {
        return res.status(400).json({ erro: 'Requisição mal formada' });
    }

    // Verifica se as senhas são iguais
    if (senha !== confSenha) {
        return res.status(422).json({ erro: 'Senhas não conferem' });
    }

    // Verifica campos obrigatórios comuns
    const camposObrigatorios = [
        'nome',
        'nascimento',
        'nacionalidade',
        'email',
        'urlFotoDocumento',
        'senha'
    ];

    for (const campo of camposObrigatorios) {
        if (!ciclista[campo]) {
            return res.status(422).json({ erro: `Campo obrigatório ausente: ${campo}`});
        }
    }

    // Validação condicional: brasileiro ou estrangeiro
    const nacionalidade = ciclista.nacionalidade?.toLowerCase();
    if (nacionalidade === 'brasileira') {
        if (!ciclista.cpf) {
            return res.status(422).json({ erro: 'CPF é obrigatório para brasileiros' });
        }
    } else if (
            !ciclista.passaporte?.numero ||
            !ciclista.passaporte?.pais
        ) {
        return res.status(422).json({ erro: 'Passaporte e país são obrigatórios para estrangeiros' });
    }
    
    try {
        const ciclistaCadastrado = await ciclistaMetodos.createCiclista(ciclista, meioDePagamento);
        res.status(201).json(ciclistaCadastrado);
    } catch (error) {
        if (error.response) {
            return res.status(error.response.status).json({
                erro: error.response.data?.mensagem || error.response.data || 'Erro na API externa'
            });
        }
        res.status(500).json({ erro: error.message || 'Erro interno do servidor' });
    }
});

router.put('/ciclista/:id', async (req, res) => {
    const {dadosCiclista} = req.body;

    const ciclistaId = req.params.id;

    if (!dadosCiclista || !ciclistaId) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }

    try {
        const ciclistaAtualizado = await ciclistaMetodos.updateCiclista(ciclistaId, dadosCiclista);
        res.status(200).json(ciclistaAtualizado);
    } catch (error) {
        console.error('Erro ao atualizar ciclista:', error);
        res.status(500).json({ erro: 'Erro interno do servidor' });
    }
});

router.get('/ciclista/:id', async (req, res) => {
    let id = req.params.id;
    if (!id) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }

    try {
        const ciclista = await ciclistaMetodos.getCiclistaById(id);
        if (!ciclista) {
            return res.status(404).json({ erro: 'Ciclista não encontrado' });
        }
        res.status(200).json(ciclista);
    } catch (error) {
        console.error('Erro ao buscar ciclista:', error);
        res.status(500).json({ erro: 'Erro interno do servidor'});
    }
});

router.delete('/ciclista/:id', async (req, res) => {
    const ciclistaId = req.params.id;

    if (!ciclistaId) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }

    try {
        let response = await ciclistaMetodos.removeCiclista(ciclistaId);
        res.status(200).json({response})
    } catch (error) {
        console.error('Erro ao deletar ciclista:', error);
        res.status(500).json({ erro: 'Erro interno do servidor' });
    }
});

router.post('/ciclista/:id/ativar', async (req, res) => {
    const ciclistaId = req.params.id;
    if (!ciclistaId) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }
    try {
        const ciclistaAtivado = await ciclistaMetodos.activateCiclista(ciclistaId);
        res.status(200).json(ciclistaAtivado);
    } catch (error) {
        console.error('Erro ao ativar ciclista:', error);
        res.status(500).json({ erro: 'Erro interno do servidor' });
    }
});

router.get('/ciclista/existeEmail/:email', async (req, res) => {
    let email = req.params.email;
    if (!email) {
        return res.status(404).json({ erro: 'Requisição mal formada' });
    }

    try {
        const existe = await ciclistaMetodos.emailExists(email);
        res.status(200).json({ existe });
    } catch (error) {
        console.error('Erro ao verificar email:', error);
        res.status(500).json({ erro: 'Erro interno do servidor' });
    }
});


module.exports = router;