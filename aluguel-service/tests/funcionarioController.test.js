const request = require('supertest');
const express = require('express');
const funcionarioRouter = require('../controllers/funcionarioController');
const funcionarioServices = require('../services/funcionario');

jest.mock('../services/funcionario');

const app = express();
app.use(express.json());
app.use(funcionarioRouter);

beforeEach(() => {
  jest.clearAllMocks();
});

describe('POST /funcionario', () => {
  it('deve criar um funcionário com sucesso', async () => {
    funcionarioServices.criaFuncionario.mockResolvedValue({ id: '1', nome: 'João' });

    const res = await request(app)
      .post('/funcionario')
      .send({ nome: 'João', email: 'joao@email.com' });

    expect(res.status).toBe(201);
    expect(res.body).toEqual({ id: '1', nome: 'João' });
  });

  it('deve retornar erro 400 para requisição mal formada', async () => {
    const res = await request(app).post('/funcionario').send({ cargo: 'Gerente' });

    expect(res.status).toBe(400);
    expect(res.body.erro).toBe('Requisição mal formada');
  });

  it('deve retornar erro 500 se der erro ao criar', async () => {
    funcionarioServices.criaFuncionario.mockImplementation(() => {
      throw new Error('Falha ao criar');
    });

    const res = await request(app)
      .post('/funcionario')
      .send({ nome: 'João', email: 'joao@email.com' });

    expect(res.status).toBe(500);
    expect(res.body.erro).toContain('Falha ao criar');
  });
});

describe('PUT /funcionario/:id', () => {
  it('deve atualizar um funcionário com sucesso', async () => {
    funcionarioServices.atualizaFuncionario.mockResolvedValue({ id: '1', nome: 'João Atualizado' });

    const res = await request(app)
      .put('/funcionario/1')
      .send({ nome: 'João Atualizado' });

    expect(res.status).toBe(200);
    expect(res.body).toEqual({ id: '1', nome: 'João Atualizado' });
  });

  it('deve retornar erro 404 para requisição mal formada', async () => {
    const res = await request(app).put('/funcionario/').send({});
    expect(res.status).toBe(404);
  });

  it('deve retornar erro 500 se der erro ao atualizar', async () => {
    funcionarioServices.atualizaFuncionario.mockImplementation(() => {
      throw new Error('Erro de teste');
    });

    const res = await request(app)
      .put('/funcionario/1')
      .send({ nome: 'Falha' });

    expect(res.status).toBe(500);
    expect(res.body.erro).toContain('Erro de teste');
  });
});

describe('GET /funcionarios', () => {
  it('deve retornar lista de funcionários com sucesso', async () => {
    funcionarioServices.retornaTodosFuncionarios.mockResolvedValue([{ id: '1', nome: 'João' }]);

    const res = await request(app).get('/funcionarios');
    expect(res.status).toBe(200);
    expect(res.body).toEqual([{ id: '1', nome: 'João' }]);
  });

  it('deve retornar erro 500 se falhar ao listar', async () => {
    funcionarioServices.retornaTodosFuncionarios.mockImplementation(() => {
      throw new Error('Erro ao buscar');
    });

    const res = await request(app).get('/funcionarios');
    expect(res.status).toBe(500);
    expect(res.body.erro).toContain('Erro ao buscar');
  });
});

describe('GET /funcionario/:id', () => {
  it('deve retornar um funcionário por ID', async () => {
    funcionarioServices.buscaFuncionarioPorId.mockResolvedValue({ id: '1', nome: 'João' });

    const res = await request(app).get('/funcionario/1');
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ id: '1', nome: 'João' });
  });

  it('deve retornar erro 404 se funcionário não for encontrado', async () => {
    funcionarioServices.buscaFuncionarioPorId.mockResolvedValue(null);

    const res = await request(app).get('/funcionario/999');
    expect(res.status).toBe(404);
    expect(res.body.erro).toBe('Funcionário não encontrado');
  });

  it('deve retornar erro 500 se der erro ao buscar funcionário', async () => {
    funcionarioServices.buscaFuncionarioPorId.mockImplementation(() => {
      throw new Error('Erro de teste');
    });

    const res = await request(app).get('/funcionario/1');
    expect(res.status).toBe(500);
    expect(res.body.erro).toContain('Erro de teste');
  });

  it('deve retornar erro 404 se id não for fornecido', async () => {
    const res = await request(app).get('/funcionario/');
    expect(res.status).toBe(404);
  });
});

describe('DELETE /funcionario/:id', () => {
  it('deve deletar um funcionário com sucesso', async () => {
    funcionarioServices.deletaFuncionario.mockResolvedValue({ mensagem: 'Funcionário deletado com sucesso' });

    const res = await request(app).delete('/funcionario/1');
    expect(res.status).toBe(200);
    expect(res.body.mensagem).toBe('Funcionário deletado com sucesso');
  });

  it('deve retornar erro 500 se der erro ao deletar', async () => {
    funcionarioServices.deletaFuncionario.mockImplementation(() => {
      throw new Error('Erro ao deletar');
    });

    const res = await request(app).delete('/funcionario/1');
    expect(res.status).toBe(500);
    expect(res.body.erro).toContain('Erro ao deletar');
  });

  it('deve retornar erro 404 se ID não for fornecido', async () => {
    const res = await request(app).delete('/funcionario/');
    expect(res.status).toBe(404);
  });
});
