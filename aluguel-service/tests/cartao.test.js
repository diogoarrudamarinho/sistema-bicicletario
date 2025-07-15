const fs = require('fs');
const cartaoService = require('../services/cartao');

jest.mock('fs');

describe('cartaoService', () => {
  const fakeDb = {
    meiosPagamento: [
      { ciclistaId: '30110', numero: '4111111111111111', validade: '2026-12', cvv: '123' },
      { ciclistaId: '99999', numero: '4222222222222222', validade: '2027-01', cvv: '321' }
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
    it('deve alterar os dados do cartão e salvar', async () => {
      fs.readFileSync.mockReturnValue(dbJson);
      fs.writeFileSync.mockImplementation(() => {});

      const novosDados = { validade: '2028-12', cvv: '999' };
      const resultado = await cartaoService.alterarCartao('30110', novosDados);

      expect(resultado).toMatchObject({ ciclistaId: '30110', validade: '2028-12', cvv: '999' });
      expect(fs.writeFileSync).toHaveBeenCalled();
    });

    it('deve lançar erro se não encontrar cartão', async () => {
      fs.readFileSync.mockReturnValue(dbJson);

      await expect(cartaoService.alterarCartao('00000', { validade: '2025-12' }))
        .rejects.toThrow('Cartão não encontrado para o ciclista');
    });

    it('deve lançar erro se não conseguir salvar', async () => {
      fs.readFileSync.mockReturnValue(dbJson);
      fs.writeFileSync.mockImplementation(() => { throw new Error('Fail write'); });

      await expect(cartaoService.alterarCartao('30110', { validade: '2025-12' }))
        .rejects.toThrow('Erro ao salvar no banco de dados');
    });

    it('deve lançar erro se não conseguir ler arquivo', async () => {
      fs.readFileSync.mockImplementation(() => { throw new Error('Fail read'); });

      await expect(cartaoService.alterarCartao('30110', { validade: '2025-12' }))
        .rejects.toThrow('Erro ao ler o banco de dados');
    });
  });
});
