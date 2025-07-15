const request = require('supertest');
const express = require('express');

jest.mock('../services/ciclista');
const ciclistaService = require('../services/ciclista');

const ciclistaRouter = require('../controllers/ciclistaController');

describe('CiclistaController', () => {
  let app;

  beforeEach(() => {
    jest.clearAllMocks();
    app = express();
    app.use(express.json());
    app.use(ciclistaRouter);
  });

  describe('POST /ciclista', () => {
    const validBody = {
      ciclista: { nome: 'Joao', cpf: '123', email: 'joao@example.com' },
      meioDePagamento: { numero: '4111', validade: '2026-12', cvv: '123' },
      senha: 'abc',
      confSenha: 'abc'
    };

    it('retorna 404 se faltar campos', async () => {
      const res = await request(app).post('/ciclista').send({});
      expect(res.status).toBe(404);
    });

    it('retorna 422 se senhas diferentes', async () => {
      const res = await request(app).post('/ciclista').send({ ...validBody, confSenha: 'diferente' });
      expect(res.status).toBe(422);
    });

    it('retorna 422 se dados do ciclista incompletos', async () => {
      const body = { ...validBody, ciclista: { nome: '', cpf: '', email: '' } };
      const res = await request(app).post('/ciclista').send(body);
      expect(res.status).toBe(422);
    });

    it('retorna 201 em sucesso', async () => {
      const mock = { id: 1, nome: 'Joao' };
      ciclistaService.cadastrarCiclista.mockResolvedValue(mock);
      const res = await request(app).post('/ciclista').send(validBody);
      expect(res.status).toBe(201);
      expect(res.body).toEqual(mock);
    });

    it('retorna 500 em erro interno', async () => {
      ciclistaService.cadastrarCiclista.mockRejectedValue(new Error('erro'));
      const res = await request(app).post('/ciclista').send(validBody);
      expect(res.status).toBe(500);
    });
  });

  describe('PUT /ciclista/:id', () => {
    it('retorna 404 se faltar dados', async () => {
      const res = await request(app).put('/ciclista/1').send({});
      expect(res.status).toBe(404);
    });

    it('retorna 200 em sucesso', async () => {
      const mock = { id: 1, nome: 'Maria' };
      ciclistaService.alteraCiclista.mockResolvedValue(mock);
      const res = await request(app).put('/ciclista/1').send({ dadosCiclista: { nome: 'Maria' } });
      expect(res.status).toBe(200);
      expect(res.body).toEqual(mock);
    });

    it('retorna 500 em erro', async () => {
      ciclistaService.alteraCiclista.mockRejectedValue(new Error('erro'));
      const res = await request(app).put('/ciclista/1').send({ dadosCiclista: { nome: 'Maria' } });
      expect(res.status).toBe(500);
    });
  });

  describe('GET /ciclista/:id', () => {
    it('retorna 404 se não encontrar', async () => {
      ciclistaService.recuperaCiclista.mockResolvedValue(null);
      const res = await request(app).get('/ciclista/1');
      expect(res.status).toBe(404);
    });

    it('retorna 200 se encontrar', async () => {
      const mock = { id: 1, nome: 'Joao' };
      ciclistaService.recuperaCiclista.mockResolvedValue(mock);
      const res = await request(app).get('/ciclista/1');
      expect(res.status).toBe(200);
      expect(res.body).toEqual(mock);
    });

    it('retorna 500 em erro', async () => {
      ciclistaService.recuperaCiclista.mockRejectedValue(new Error('erro'));
      const res = await request(app).get('/ciclista/1');
      expect(res.status).toBe(500);
    });
  });

  describe('DELETE /ciclista/:id', () => {
    it('retorna 200 em sucesso', async () => {
      ciclistaService.removeCiclista.mockResolvedValue({});
      const res = await request(app).delete('/ciclista/1');
      expect(res.status).toBe(200);
    });

    it('retorna 500 em erro', async () => {
      ciclistaService.removeCiclista.mockRejectedValue(new Error('erro'));
      const res = await request(app).delete('/ciclista/1');
      expect(res.status).toBe(500);
    });
  });

  describe('POST /ciclista/:id/ativar', () => {
    it('retorna 200 em sucesso', async () => {
      const mock = { id: 1, ativado: true };
      ciclistaService.ativarCiclista.mockResolvedValue(mock);
      const res = await request(app).post('/ciclista/1/ativar');
      expect(res.status).toBe(200);
      expect(res.body).toEqual(mock);
    });

    it('retorna 500 em erro', async () => {
      ciclistaService.ativarCiclista.mockRejectedValue(new Error('erro'));
      const res = await request(app).post('/ciclista/1/ativar');
      expect(res.status).toBe(500);
    });
  });

  describe('GET /ciclista/existeEmail/:email', () => {
    it('retorna 200 em sucesso', async () => {
      ciclistaService.existeEmail.mockResolvedValue(true);
      const res = await request(app).get('/ciclista/existeEmail/test@example.com');
      expect(res.status).toBe(200);
      expect(res.body).toEqual({ existe: true });
    });

    it('retorna 500 em erro', async () => {
      ciclistaService.existeEmail.mockRejectedValue(new Error('erro'));
      const res = await request(app).get('/ciclista/existeEmail/test@example.com');
      expect(res.status).toBe(500);
    });
  });
});
