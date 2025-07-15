// tests/cartaoController.test.js

const request = require('supertest');
const express = require('express');
const { beforeEach, describe, it, expect } = require('@jest/globals');

// Mock services
jest.mock('../services/ciclista');
jest.mock('../services/cartao');
const cartaoService = require('../services/cartao');
const ciclistaService = require('../services/ciclista');

// Controllers
const cartaoController = require('../controllers/cartaoController');
const ciclistaRouter = require('../controllers/ciclistaController');

function createAppWith(controller) {
    const app = express();
    app.use(express.json());
    app.use(controller);
    return app;
}

// ======================== CartãoController ========================
describe('CartãoController', () => {
    let app;

    beforeEach(() => {
        jest.clearAllMocks();
        app = createAppWith(cartaoController);
    });

    describe('GET /cartaoDeCredito/:idCiclista', () => {
        it('retorna 404 se idCiclista não for fornecido', async () => {
            const res = await request(app).get('/cartaoDeCredito/');
            expect(res.status).toBe(404);
            expect(res.body).toEqual({ erro: 'Requisição mal formada' });
        });

        it('retorna 200 e o cartão em caso de sucesso', async () => {
            const mockCartao = { id: '39044', numero: '4111111111111111' };
            cartaoService.recuperaCartao.mockResolvedValue(mockCartao);

            const res = await request(app).get('/cartaoDeCredito/30110');
            expect(cartaoService.recuperaCartao).toHaveBeenCalledWith('30110');
            expect(res.status).toBe(200);
            expect(res.body).toEqual(mockCartao);
        });

        it('retorna 500 em erro interno', async () => {
            cartaoService.recuperaCartao.mockRejectedValue(new Error('erro'));
            const res = await request(app).get('/cartaoDeCredito/30110');
            expect(res.status).toBe(500);
            expect(res.body).toEqual({ erro: 'Erro interno do servidor' });
        });
    });

    describe('PUT /cartaoDeCredito/:idCiclista', () => {
        it('retorna 404 se dados faltarem', async () => {
            const res1 = await request(app).put('/cartaoDeCredito/');
            expect(res1.status).toBe(404);
            expect(res1.body).toEqual({ erro: 'Requisição mal formada' });

            const res2 = await request(app).put('/cartaoDeCredito/30110').send();
            expect(res2.status).toBe(404);
            expect(res2.body).toEqual({ erro: 'Requisição mal formada' });
        });

        it('retorna 200 em sucesso', async () => {
            const update = { validade: '2027-04', cvv: '321' };
            const mock = { id: '39044', ...update };
            cartaoService.alterarCartao.mockResolvedValue(mock);

            const res = await request(app).put('/cartaoDeCredito/30110').send(update);
            expect(res.status).toBe(200);
            expect(res.body).toEqual(mock);
        });

        it('retorna 500 em erro interno', async () => {
            cartaoService.alterarCartao.mockRejectedValue(new Error('erro'));
            const res = await request(app).put('/cartaoDeCredito/30110').send({});
            expect(res.status).toBe(500);
            expect(res.body).toEqual({ erro: 'Erro interno do servidor' });
        });
    });
});
