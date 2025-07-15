// tests/ciclistaService.test.js
const {
  createCiclista,
  updateCiclista,
  getCiclistaById,
  activateCiclista,
  emailExists,
  removeCiclista
} = require('../services/ciclista');

const db = require('../repositories/acessoDB/ciclistaDB');
const validacao = require('../services/validacao');
const { beforeEach, describe, it, expect } = require('@jest/globals');

jest.mock('../repositories/acessoDB/ciclistaDB');

describe('ciclistaService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('createCiclista', () => {
    const ciclista = { nome: 'Ana', cpf: '12345678901', email: 'ana@example.com', nascimento: '1990-01-01' };
    const meioDePagamento = { numero: '4111111111111111', validade: '2026-12', cvv: '123' };

    it('deve lançar erro se email já estiver cadastrado', async () => {
      db.existeEmail.mockResolvedValue(true);
      await expect(createCiclista(ciclista, meioDePagamento))
        .rejects.toThrow('Erro ao cadastrar ciclista: Email já cadastrado');
    });

    it('deve criar ciclista chamando o repositório', async () => {
      db.existeEmail.mockResolvedValue(false);
      const mockResult = { id: '1', ...ciclista };
      db.criarCiclista.mockResolvedValue(mockResult);

      const result = await createCiclista(ciclista, meioDePagamento);

      expect(db.existeEmail).toHaveBeenCalledWith(ciclista.email);
      expect(db.criarCiclista).toHaveBeenCalledWith(ciclista, meioDePagamento);
      expect(result).toBe(mockResult);
    });

    it('deve lançar erro se validarEmail lançar exceção', async () => {
      jest.spyOn(validacao, 'validarEmail').mockImplementation(() => { throw new Error('Email inválido'); });

      await expect(createCiclista(ciclista, meioDePagamento))
        .rejects.toThrow('Erro ao cadastrar ciclista: Email inválido');

      validacao.validarEmail.mockRestore();
    });

    it('deve lançar erro se validarCPF lançar exceção', async () => {
      jest.spyOn(validacao, 'validarCPF').mockImplementation(() => { throw new Error('CPF inválido'); });

      await expect(createCiclista(ciclista, meioDePagamento))
        .rejects.toThrow('Erro ao cadastrar ciclista: CPF inválido');

      validacao.validarCPF.mockRestore();
    });
  });

  describe('getCiclistaById', () => {
    it('deve lançar erro se não encontrar', async () => {
      db.obterCiclista.mockResolvedValue(undefined);
      await expect(getCiclistaById('999')).rejects.toThrow('Ciclista não encontrado');
    });

    it('deve retornar ciclista em sucesso', async () => {
      const mockC = { id: '1', nome: 'Joao' };
      db.obterCiclista.mockResolvedValue(mockC);
      const result = await getCiclistaById('1');
      expect(result).toBe(mockC);
    });
  });

  describe('updateCiclista', () => {
    it('deve chamar o repositório e retornar resultado', async () => {
      const mockUpdated = { id: '1', nome: 'Maria' };
      db.atualizarCiclista.mockResolvedValue(mockUpdated);
      const result = await updateCiclista('1');
      expect(db.atualizarCiclista).toHaveBeenCalledWith('1');
      expect(result).toBe(mockUpdated);
    });
  });

  describe('removeCiclista', () => {
    it('deve chamar o repositório e retornar mensagem', async () => {
      const mockRem = { mensagem: 'ok' };
      db.deletarCiclista.mockResolvedValue(mockRem);
      const result = await removeCiclista('1');
      expect(db.deletarCiclista).toHaveBeenCalledWith('1');
      expect(result).toBe(mockRem);
    });
  });

  describe('activateCiclista', () => {
    it('deve ativar ciclista', async () => {
      const mockA = { id: '1', ativado: true };
      db.ativarCiclista.mockResolvedValue(mockA);
      const result = await activateCiclista('1');
      expect(db.ativarCiclista).toHaveBeenCalledWith('1');
      expect(result).toBe(mockA);
    });
  });

  describe('emailExists', () => {
    it('deve retornar true ou false', async () => {
      db.existeEmail.mockResolvedValue(true);
      const exists = await emailExists('x@x.com');
      expect(db.existeEmail).toHaveBeenCalledWith('x@x.com');
      expect(exists).toBe(true);
    });
  });
});
