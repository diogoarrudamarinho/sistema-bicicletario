const database = require('../repositories/acessoDB/aluguelDB');
const { alugarBicicleta, devolverBicicleta } = require('../services/aluguel');
const { beforeEach, describe, it, expect } = require('@jest/globals');

// Mocks
jest.mock('../repositories/acessoDB/aluguelDB');

describe('alugarBicicleta', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('deve alugar uma bicicleta com sucesso', async () => {
        database.recuperaBicicletaPorTranca.mockResolvedValue({ id: '1', status: 'disponível' });
        database.verificaAluguelAtivo.mockResolvedValue(false);
        database.registraAluguel.mockResolvedValue();

        await expect(alugarBicicleta('ciclista1', 'tranca1')).resolves.not.toThrow();
        expect(database.recuperaBicicletaPorTranca).toHaveBeenCalledWith('tranca1');
        expect(database.verificaAluguelAtivo).toHaveBeenCalledWith('ciclista1');
        expect(database.registraAluguel).toHaveBeenCalled();
    });

    it('deve lançar erro se a bicicleta não estiver disponível', async () => {
        database.recuperaBicicletaPorTranca.mockResolvedValue({ id: '1', status: 'indisponível' });

        await expect(alugarBicicleta('ciclista1', 'tranca1')).rejects.toThrow('Bicicleta não disponível para aluguel');
    });

    it('deve lançar erro se o ciclista já tiver um aluguel ativo', async () => {
        database.recuperaBicicletaPorTranca.mockResolvedValue({ id: '1', status: 'disponível' });
        database.verificaAluguelAtivo.mockResolvedValue(true);

        await expect(alugarBicicleta('ciclista1', 'tranca1')).rejects.toThrow('Já possui um aluguel ativo.');
    });
});

describe('devolverBicicleta', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('deve devolver uma bicicleta com sucesso', async () => {
        database.verificaCiclistaComBicicleta.mockResolvedValue({ id: 'aluguel1' });
        database.finalizarAluguel.mockResolvedValue();
        database.atualizarStatusBicicleta.mockResolvedValue();

        const res = await devolverBicicleta('tranca1', 'bicicleta1');
        expect(res.mensagem).toBe('Bicicleta devolvida com sucesso');
        expect(database.finalizarAluguel).toHaveBeenCalled();
        expect(database.atualizarStatusBicicleta).toHaveBeenCalledWith('bicicleta1', 'disponível');
    });

    it('deve lançar erro se não houver aluguel ativo para a bicicleta', async () => {
        database.verificaCiclistaComBicicleta.mockResolvedValue(null);

        await expect(devolverBicicleta('tranca1', 'bicicleta1')).rejects.toThrow('Nenhum aluguel ativo encontrado para essa bicicleta');
    });
});