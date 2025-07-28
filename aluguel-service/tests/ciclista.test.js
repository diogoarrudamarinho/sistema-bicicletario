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
const axios = require('axios');

jest.mock('../repositories/acessoDB/ciclistaDB');
jest.mock('axios');

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
        .rejects.toThrow('Email já cadastrado');
    });

    it('deve criar ciclista chamando o repositório', async () => {
      db.existeEmail.mockResolvedValue(false);
      const mockResult = { id: '1', ...ciclista };
      db.criarCiclista.mockResolvedValue(mockResult);
      axios.post.mockResolvedValue({}); // mock validação + envio

      const result = await createCiclista(ciclista, meioDePagamento);

      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/cartao/validar'),
        meioDePagamento
      );
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/enviarEmail'),
        expect.objectContaining({
          destinatario: ciclista.email,
          assunto: expect.any(String)
        }),
        expect.any(Object)
      );
      expect(db.criarCiclista).toHaveBeenCalledWith(ciclista, meioDePagamento);
      expect(result).toBe(mockResult);
    });

    it('deve lançar erro se validação do cartão falhar', async () => {
      axios.post.mockRejectedValueOnce(new Error('Falha ao validar cartão'));

      await expect(createCiclista(ciclista, meioDePagamento))
        .rejects.toThrow('Falha ao validar cartão');
    });

    it('deve lançar erro se envio de email falhar', async () => {
      db.existeEmail.mockResolvedValue(false);
      const mockResult = { id: '1', ...ciclista };
      db.criarCiclista.mockResolvedValue(mockResult);
      axios.post
        .mockResolvedValueOnce({}) // validação cartão
        .mockRejectedValueOnce(new Error('Falha ao enviar email'));

      await expect(createCiclista(ciclista, meioDePagamento))
        .rejects.toThrow('Falha ao enviar email');
    });

    it('deve lançar erro se validarEmail lançar exceção', async () => {
      jest.spyOn(validacao, 'validarEmail').mockImplementation(() => {
        throw new Error('Email inválido');
      });

      await expect(createCiclista(ciclista, meioDePagamento))
        .rejects.toThrow('Email inválido');

      validacao.validarEmail.mockRestore();
    });

    it('deve lançar erro se validarCPF lançar exceção', async () => {
      jest.spyOn(validacao, 'validarCPF').mockImplementation(() => {
        throw new Error('CPF inválido');
      });

      await expect(createCiclista(ciclista, meioDePagamento))
        .rejects.toThrow('CPF inválido');

      validacao.validarCPF.mockRestore();
    });
  });

  describe('getCiclistaById', () => {
    it('deve lançar erro se repositório falhar', async () => {
      db.obterCiclista.mockImplementation(() => { throw new Error('Falhou'); });

      await expect(getCiclistaById('1')).rejects.toThrow('Erro ao recuperar ciclista: Falhou');
    });

    it('deve retornar ciclista com sucesso', async () => {
      const mockC = { id: '1', nome: 'Joao' };
      db.obterCiclista.mockResolvedValue(mockC);

      const result = await getCiclistaById('1');

      expect(result).toBe(mockC);
    });
  });

  describe('updateCiclista', () => {
    it('deve chamar o repositório e enviar email', async () => {
      const updated = { id: '1', nome: 'Maria', email: 'maria@example.com' };
      db.atualizarCiclista.mockResolvedValue(updated);
      axios.post.mockResolvedValue({});

      const result = await updateCiclista('1');

      expect(db.atualizarCiclista).toHaveBeenCalledWith('1');
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/enviarEmail'),
        expect.objectContaining({
          destinatario: updated.email,
          mensagem: expect.stringContaining('Dados Alterados')
        }),
        expect.any(Object)
      );
      expect(result).toBe(updated);
    });

    it('deve lançar erro se envio de email falhar', async () => {
      const updated = { id: '1', email: 'maria@example.com' };
      db.atualizarCiclista.mockResolvedValue(updated);
      axios.post.mockRejectedValueOnce(new Error('Erro no email'));

      await expect(updateCiclista('1')).rejects.toThrow('Erro no email');
    });
  });

  describe('removeCiclista', () => {
    it('deve chamar o repositório e enviar email', async () => {
      const mockRem = { email: 'removido@example.com' };
      db.deletarCiclista.mockResolvedValue(mockRem);
      axios.post.mockResolvedValue({});

      const result = await removeCiclista('1');

      expect(db.deletarCiclista).toHaveBeenCalledWith('1');
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/enviarEmail'),
        expect.objectContaining({
          destinatario: mockRem.email,
          mensagem: expect.any(String)
        }),
        expect.any(Object)
      );
      expect(result).toBe(mockRem);
    });

    it('deve lançar erro se envio de email falhar', async () => {
      db.deletarCiclista.mockResolvedValue({ email: 'x@x.com' });
      axios.post.mockRejectedValueOnce(new Error('Falhou envio'));

      await expect(removeCiclista('1')).rejects.toThrow('Falhou envio');
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

    it('deve lançar erro se repositório falhar', async () => {
      db.ativarCiclista.mockImplementation(() => { throw new Error('Falha ativar'); });

      await expect(activateCiclista('1')).rejects.toThrow('Erro ao ativar ciclista: Falha ativar');
    });
  });

  describe('emailExists', () => {
    it('deve retornar true ou false', async () => {
      db.existeEmail.mockResolvedValue(true);
      const exists = await emailExists('x@x.com');
      expect(db.existeEmail).toHaveBeenCalledWith('x@x.com');
      expect(exists).toBe(true);
    });

    it('deve lançar erro se repositório falhar', async () => {
      db.existeEmail.mockImplementation(() => { throw new Error('Falha email'); });

      await expect(emailExists('x@x.com')).rejects.toThrow('Erro ao verificar email: Falha email');
    });
  });
});
