const fs = require('fs');
const axios = require('axios');
const cartaoService = require('../services/cartao');

jest.mock('fs');
jest.mock('axios');

describe('cartaoService', () => {
  const fakeDb = {
    meiosPagamento: [
      { ciclistaId: '30110', numero: '4111111111111111', validade: '2026-12', cvv: '123' },
      { ciclistaId: '99999', numero: '4222222222222222', validade: '2027-01', cvv: '321' }
    ],
    ciclistas: [
      { id: '30110', email: 'joao@example.com' }
    ]
  };
  const dbJson = JSON.stringify(fakeDb);

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('recuperaCartao', () => {
    it('deve retornar o cartão existente', async () => {
      fs.readFileSync.mockReturnValue(dbJson);

      const cartao = await cartaoService.recuperaCartao('30110');

      expect(cartao).toEqual(fakeDb.meiosPagamento[0]);
    });

    it('deve lançar erro se arquivo não puder ser lido', async () => {
      fs.readFileSync.mockImplementation(() => { throw new Error('Fail read'); });

      await expect(cartaoService.recuperaCartao('30110')).rejects.toThrow('Erro ao ler o banco de dados');
    });

    it('deve lançar erro se cartão não for encontrado', async () => {
      fs.readFileSync.mockReturnValue(dbJson);

      await expect(cartaoService.recuperaCartao('00000')).rejects.toThrow('Cartão não encontrado');
    });
  });

  describe('alterarCartao', () => {
    it('deve alterar os dados do cartão e salvar com sucesso', async () => {
      fs.readFileSync.mockReturnValue(dbJson);
      fs.writeFileSync.mockImplementation(() => {});
      axios.post.mockResolvedValue({}); // Mock das chamadas externas

      const novosDados = { validade: '2028-12', cvv: '999' };
      const resultado = await cartaoService.alterarCartao('30110', novosDados);

      expect(resultado).toMatchObject({ ciclistaId: '30110', validade: '2028-12', cvv: '999' });
      expect(fs.writeFileSync).toHaveBeenCalled();
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/validaCartaoDeCredito'),
        expect.objectContaining({ ciclistaId: '30110' })
      );
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/enviarEmail'),
        expect.objectContaining({ destinatario: 'joao@example.com' }),
        expect.any(Object) // Headers
      );
    });

    it('deve lançar erro se não encontrar cartão', async () => {
      fs.readFileSync.mockReturnValue(dbJson);

      await expect(cartaoService.alterarCartao('00000', { validade: '2025-12' }))
        .rejects.toThrow('Cartão não encontrado para o ciclista');
    });

    it('deve lançar erro se não conseguir salvar', async () => {
      fs.readFileSync.mockReturnValue(dbJson);
      fs.writeFileSync.mockImplementation(() => { throw new Error('Fail write'); });
      axios.post.mockResolvedValue({}); // garantir que o erro venha do fs, não axios

      await expect(cartaoService.alterarCartao('30110', { validade: '2025-12' }))
        .rejects.toThrow('Erro ao salvar no banco de dados');
    });

    it('deve lançar erro se não conseguir ler arquivo', async () => {
      fs.readFileSync.mockImplementation(() => { throw new Error('Fail read'); });

      await expect(cartaoService.alterarCartao('30110', { validade: '2025-12' }))
        .rejects.toThrow('Erro ao ler o banco de dados');
    });

    it('deve lançar erro se validação do cartão falhar', async () => {
      fs.readFileSync.mockReturnValue(dbJson);
      fs.writeFileSync.mockImplementation(() => {});
      axios.post
        .mockRejectedValueOnce(new Error('Falha validação'))
        .mockResolvedValueOnce({}); // ignorar envio de email

      await expect(cartaoService.alterarCartao('30110', { validade: '2025-12' }))
        .rejects.toThrow('Falha validação');
    });

    it('deve lançar erro se envio de email falhar', async () => {
      fs.readFileSync.mockReturnValue(dbJson);
      fs.writeFileSync.mockImplementation(() => {});
      axios.post
        .mockResolvedValueOnce({}) // validação ok
        .mockRejectedValueOnce(new Error('Falha envio de email'));

      await expect(cartaoService.alterarCartao('30110', { validade: '2025-12' }))
        .rejects.toThrow('Falha envio de email');
    });
  });
});
