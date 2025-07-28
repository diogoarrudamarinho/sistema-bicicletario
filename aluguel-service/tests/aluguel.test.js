const { alugarBicicleta, devolverBicicleta } = require('../services/aluguel');
const database = require('../repositories/acessoDB/aluguelDB');
const ciclistaDB = require('../repositories/acessoDB/ciclistaDB');
const axios = require('axios');

jest.mock('../repositories/acessoDB/aluguelDB');
jest.mock('../repositories/acessoDB/ciclistaDB');
jest.mock('axios');

describe('alugarBicicleta', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve alugar bicicleta com sucesso', async () => {
    axios.get.mockResolvedValue({ data: { id: 'bicicleta1', status: 'DISPONIVEL' } });
    database.verificaAluguelAtivo.mockResolvedValue(false);
    database.registraAluguel.mockResolvedValue({ ok: true });
    axios.post.mockResolvedValue({});
    ciclistaDB.recuperaCiclista.mockResolvedValue({ email: 'ciclista@email.com', id: 'ciclista1' });

    const result = await alugarBicicleta('ciclista1', 'tranca1');
    expect(database.verificaAluguelAtivo).toHaveBeenCalledWith('ciclista1');
    expect(database.registraAluguel).toHaveBeenCalled();
    expect(result).toEqual({ ok: true });
  });

  it('deve lançar erro se a bicicleta não estiver disponível', async () => {
    axios.get.mockResolvedValue({ data: { id: 'bicicleta1', status: 'OCUPADA' } });

    await expect(alugarBicicleta('ciclista1', 'tranca1'))
      .rejects.toThrow('Bicicleta não disponível para aluguel');
  });

  it('deve lançar erro se já houver aluguel ativo', async () => {
    axios.get.mockResolvedValue({ data: { id: 'bicicleta1', status: 'DISPONIVEL' } });
    database.verificaAluguelAtivo.mockResolvedValue(true);

    await expect(alugarBicicleta('ciclista1', 'tranca1'))
      .rejects.toThrow('Já possui um aluguel ativo');
  });
});

describe('devolverBicicleta', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve devolver a bicicleta com sucesso sem cobrança extra', async () => {
    axios.get.mockResolvedValue({ data: { id: 'bicicleta1' } });
    database.verificaCiclistaComBicicleta.mockResolvedValue('ciclista1');
    database.recuperarAluguelAtivoPorCiclista.mockResolvedValue({
      horaInicio: new Date(Date.now() - 60 * 60 * 1000).toISOString() // 1h antes
    });
    database.finalizarAluguel.mockResolvedValue({ ok: true });
    ciclistaDB.recuperaCiclista.mockResolvedValue({ email: 'x@x.com', id: 'ciclista1' });
    axios.post.mockResolvedValue({});

    const result = await devolverBicicleta('tranca1', 'bicicleta1');
    expect(result.mensagem).toBe('Bicicleta devolvida com sucesso');
  });

  it('deve cobrar adicional se uso for maior que 2h', async () => {
    axios.get.mockResolvedValue({ data: { id: 'bicicleta1' } });
    database.verificaCiclistaComBicicleta.mockResolvedValue('ciclista1');
    database.recuperarAluguelAtivoPorCiclista.mockResolvedValue({
      horaInicio: new Date(Date.now() - 3 * 60 * 60 * 1000).toISOString() // 3h antes
    });
    database.finalizarAluguel.mockResolvedValue({ ok: true });
    ciclistaDB.recuperaCiclista.mockResolvedValue({ email: 'x@x.com', id: 'ciclista1' });
    axios.post.mockResolvedValue({});

    const result = await devolverBicicleta('tranca1', 'bicicleta1');
    expect(result.mensagem).toBe('Bicicleta devolvida com sucesso');
  });

  it('deve lançar erro se não encontrar aluguel para bicicleta', async () => {
    axios.get.mockResolvedValue({ data: { id: 'bicicleta1' } });
    database.verificaCiclistaComBicicleta.mockResolvedValue(null);

    await expect(devolverBicicleta('tranca1', 'bicicleta1'))
      .rejects.toThrow('Nenhum aluguel ativo encontrado para essa bicicleta');
  });

  it('deve lançar erro se aluguel ativo estiver incompleto', async () => {
    axios.get.mockResolvedValue({ data: { id: 'bicicleta1' } });
    database.verificaCiclistaComBicicleta.mockResolvedValue('ciclista1');
    database.recuperarAluguelAtivoPorCiclista.mockResolvedValue(null);

    await expect(devolverBicicleta('tranca1', 'bicicleta1'))
      .rejects.toThrow('Aluguel ativo não encontrado ou incompleto');
  });
});
