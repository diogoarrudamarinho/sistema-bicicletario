const request = require('supertest');
const express = require('express');
const aluguelRouter = require('../controllers/aluguelController');
const aluguelServices = require('../services/aluguel');

jest.mock('../services/aluguel');

const app = express();
app.use(express.json());
app.use(aluguelRouter);

describe('POST /aluguel', () => {
    beforeEach(() => jest.clearAllMocks());

    it('deve retornar 404 para requisição mal formada', async () => {
        const res = await request(app).post('/aluguel').send({ idCiclista: '1' });
        expect(res.status).toBe(404);
        expect(res.body.erro).toBe('Requisição mal formada');
    });

    it('deve alugar uma bicicleta com sucesso', async () => {
        const mockAluguel = { mensagem: 'Aluguel realizado com sucesso' };
        aluguelServices.alugarBicicleta.mockResolvedValue(mockAluguel);

        const res = await request(app)
            .post('/aluguel')
            .send({ idCiclista: '1', idTranca: '2' });

        expect(res.status).toBe(201);
        expect(res.body).toEqual(mockAluguel);
        expect(aluguelServices.alugarBicicleta).toHaveBeenCalledWith('1', '2');
    });

    it('deve retornar 500 se o serviço falhar', async () => {
        aluguelServices.alugarBicicleta.mockRejectedValue(new Error('Service failure'));

        const res = await request(app)
            .post('/aluguel')
            .send({ idCiclista: '1', idTranca: '2' });

        expect(res.status).toBe(500);
        expect(res.body.erro).toBe('Service failure');
    });
});

describe('POST /devolucao', () => {
    beforeEach(() => jest.clearAllMocks());

    it('deve retornar 404 para requisição mal formada', async () => {
        const res = await request(app).post('/devolucao').send({ idTranca: '1' });
        expect(res.status).toBe(404);
        expect(res.body.erro).toBe('Requisição mal formada');
    });

    it('deve devolver uma bicicleta com sucesso', async () => {
        const mockResult = { mensagem: 'Bicicleta devolvida com sucesso' };
        aluguelServices.devolverBicicleta.mockResolvedValue(mockResult);

        const res = await request(app)
            .post('/devolucao')
            .send({ idTranca: '1', idBicicleta: '2' });

        expect(res.status).toBe(200);
        expect(res.body).toEqual(mockResult);
        expect(aluguelServices.devolverBicicleta).toHaveBeenCalledWith('1', '2');
    });

    it('deve retornar 500 se o serviço de devolução falhar', async () => {
        aluguelServices.devolverBicicleta.mockRejectedValue(new Error('Devolucao failure'));

        const res = await request(app)
            .post('/devolucao')
            .send({ idTranca: '1', idBicicleta: '2' });

        expect(res.status).toBe(500);
        expect(res.body.erro).toBe('Devolucao failure');
    });
});
