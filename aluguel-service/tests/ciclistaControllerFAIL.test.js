const request = require('supertest');
const express = require('express');
const ciclistaRouter = require('../controllers/ciclistaController');

jest.mock('../services/ciclista');
const ciclistaService = require('../services/ciclista');

let app;

beforeEach(() => {
  jest.clearAllMocks();
  app = express();
  app.use(express.json());
  app.use('/ciclista', ciclistaRouter); // Prefixo definido corretamente aqui
});

describe('DELETE /ciclista/:id', () => {
  it('retorna 404 se id não for fornecido (rota não encontrada)', async () => {
    const res = await request(app).delete('/ciclista');
    expect(res.status).toBe(404);
  });

  it('retorna 404 se id for vazio', async () => {
    const res = await request(app).delete('/ciclista/');
    expect(res.status).toBe(404);
    expect(res.body).toEqual({ erro: 'Requisição mal formada' });
  });

  it('retorna 200 em sucesso', async () => {
    ciclistaService.removeCiclista.mockResolvedValue('removido');
    const res = await request(app).delete('/ciclista/123');
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ response: 'removido' });
  });

  it('retorna 500 em erro interno', async () => {
    ciclistaService.removeCiclista.mockRejectedValue(new Error('fail'));
    const res = await request(app).delete('/ciclista/123');
    expect(res.status).toBe(500);
    expect(res.body).toEqual({ erro: 'Erro interno do servidor' });
  });
});

describe('POST /ciclista/:id/ativar', () => {
  it('retorna 404 se id inválido (rota não encontrada)', async () => {
    const res = await request(app).post('/ciclista//ativar');
    expect(res.status).toBe(404);
  });

  it('retorna 404 se id for vazio', async () => {
    const res = await request(app).post('/ciclista/ /ativar');
    expect(res.status).toBe(404);
    expect(res.body).toEqual({ erro: 'Requisição mal formada' });
  });

  it('retorna 200 em sucesso', async () => {
    const mock = { id: '123', ativado: true };
    ciclistaService.ativarCiclista.mockResolvedValue(mock);
    const res = await request(app).post('/ciclista/123/ativar');
    expect(res.status).toBe(200);
    expect(res.body).toEqual(mock);
  });

  it('retorna 500 em erro interno', async () => {
    ciclistaService.ativarCiclista.mockRejectedValue(new Error('fail'));
    const res = await request(app).post('/ciclista/123/ativar');
    expect(res.status).toBe(500);
    expect(res.body).toEqual({ erro: 'Erro interno do servidor' });
  });
});

describe('GET /ciclista/existeEmail/:email', () => {
  it('retorna 404 se email não for fornecido', async () => {
    const res = await request(app).get('/ciclista/existeEmail/');
    expect(res.status).toBe(404);
  });

  it('retorna 404 se email for vazio', async () => {
    const res = await request(app).get('/ciclista/existeEmail//');
    expect(res.status).toBe(404);
    expect(res.body).toEqual({ erro: 'Requisição mal formada' });
  });

  it('retorna 200 em sucesso', async () => {
    ciclistaService.existeEmail.mockResolvedValue(true);
    const res = await request(app).get('/ciclista/existeEmail/test@example.com');
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ existe: true });
  });

  it('retorna 500 em erro interno', async () => {
    ciclistaService.existeEmail.mockRejectedValue(new Error('fail'));
    const res = await request(app).get('/ciclista/existeEmail/test@example.com');
    expect(res.status).toBe(500);
    expect(res.body).toEqual({ erro: 'Erro interno do servidor' });
  });
});
