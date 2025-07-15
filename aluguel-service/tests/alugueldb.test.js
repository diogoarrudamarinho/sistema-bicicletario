// tests/aluguelDB.test.js
const fs = require('fs/promises');
const path = require('path');

jest.mock('fs/promises');

const {
    recuperaBicicletaPorTranca,
    verificaAluguelAtivo,
    registraAluguel,
    finalizarAluguel,
    atualizarStatusBicicleta,
    verificaCiclistaComBicicleta
} = require('../repositories/acessoDB/aluguelDB');

describe('aluguelDB', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    describe('recuperaBicicletaPorTranca', () => {
        it('deve lançar erro se tranca não existir', async () => {
            const trancas = { trancas: [] };
            fs.readFile.mockResolvedValue(JSON.stringify(trancas));

            await expect(recuperaBicicletaPorTranca('T001'))
                .rejects.toThrow('Tranca não encontrada');
        });

        it('deve retornar bicicleta disponível', async () => {
            const trancas = { trancas: [{ id: 'T001', bicicleta: 'B1' }] };
            const bicicletas = { bicicletas: [{ id: 'B1', status: 'disponível' }] };

            // first call for trancas, second for bicicletas
            fs.readFile
                .mockResolvedValueOnce(JSON.stringify(trancas))
                .mockResolvedValueOnce(JSON.stringify(bicicletas));

            const bike = await recuperaBicicletaPorTranca('T001');
            expect(bike).toEqual({ id: 'B1', status: 'disponível' });
        });

        it('deve lançar erro se bicicleta não disponível', async () => {
            const trancas = { trancas: [{ id: 'T001', bicicleta: 'B1' }] };
            const bicicletas = { bicicletas: [{ id: 'B1', status: 'indisponível' }] };
            fs.readFile
                .mockResolvedValueOnce(JSON.stringify(trancas))
                .mockResolvedValueOnce(JSON.stringify(bicicletas));

            await expect(recuperaBicicletaPorTranca('T001'))
                .rejects.toThrow('Bicicleta não encontrada ou não disponível');
        });
    });

    describe('verificaAluguelAtivo', () => {
        it('deve retornar aluguel ativo quando existir', async () => {
            const alugueis = { alugueis: [
                    { ciclista: 'C1', horaFim: null }
                ]};
            fs.readFile.mockResolvedValue(JSON.stringify(alugueis));

            const ativo = await verificaAluguelAtivo('C1');
            expect(ativo).toEqual({ ciclista: 'C1', horaFim: null });
        });

       it('deve retornar null quando não houver ativo', async () => {
    const alugueis = { alugueis: [
            { ciclista: 'C1', horaFim: '2025-07-05T10:00:00Z' }
        ]};
    fs.readFile.mockResolvedValue(JSON.stringify(alugueis));

    const ativo = await verificaAluguelAtivo('C1');
    expect(ativo).toBeNull();  // porque sua função retorna null nesse caso
});

    });

    describe('registraAluguel', () => {
        it('deve registrar aluguel e atualizar status e escrever arquivos', async () => {
            // mocks
            const trancas = { trancas: [{ id: 'T1', bicicleta: 'B1' }] };
            const bicicletasJson = { bicicletas: [{ id: 'B1', status: 'disponível' }] };
            const alugueisJson = { alugueis: [] };

            fs.readFile
                .mockResolvedValueOnce(JSON.stringify(trancas))  // trancas
                .mockResolvedValueOnce(JSON.stringify(bicicletasJson)) // bicicletas
                .mockResolvedValueOnce(JSON.stringify(alugueisJson)); // alugueis
            fs.writeFile.mockResolvedValue();

            const now = '2025-07-06T12:00:00.000Z';
            const novo = await registraAluguel('C1', 'B1', now);

            expect(novo).toMatchObject({ bicicleta: 'B1', horaInicio: now, ciclista: 'C1' });
            // duas escritas: bicicletas e alugueis
            expect(fs.writeFile).toHaveBeenCalledTimes(2);
        });
    });

   describe('finalizarAluguel', () => {
    it('deve finalizar aluguel e atualizar cobranças', async () => {
        // historico com um aluguel sem horaFim
        const alugueis = { alugueis: [
                { ciclista: 'C1', bicicleta: 'B1', horaInicio: 't1', horaFim: null, trancaFim: null, cobranca: null }
            ]};
        fs.readFile.mockResolvedValue(JSON.stringify(alugueis));
        fs.writeFile.mockResolvedValue();

        // Passar o aluguel ativo, trancaFim e horaFim
        const aluguel = alugueis.alugueis[0];
        const trancaFim = 'T2';
        const horaFim = new Date().toISOString();

        const result = await finalizarAluguel(aluguel, trancaFim, horaFim);
        expect(result).toHaveProperty('horaFim', horaFim);
        expect(result).toHaveProperty('trancaFim', trancaFim);
        expect(fs.writeFile).toHaveBeenCalled();
    });
});

    describe('atualizarStatusBicicleta', () => {
        it('deve atualizar status da bicicleta', async () => {
            const bicicletasJson = { bicicletas: [{ id: 'B1', status: 'disponível' }] };
            fs.readFile.mockResolvedValue(JSON.stringify(bicicletasJson));
            fs.writeFile.mockResolvedValue();

            await atualizarStatusBicicleta('B1', 'em uso');
            expect(fs.writeFile).toHaveBeenCalled();
        });
    });

    describe('verificaCiclistaComBicicleta', () => {
       it('deve retornar aluguel se existir ativo para bicicleta', async () => {
    const alugueis = { alugueis: [ { ciclista: 'C1', bicicleta: 'B1', horaFim: null } ] };
    fs.readFile.mockResolvedValue(JSON.stringify(alugueis));

    const res = await verificaCiclistaComBicicleta('B1');
    expect(res).toBe('C1');  // só o ID do ciclista, que a função retorna
});


        it('deve retornar null se nao houver', async () => {
            const alugueis = { alugueis: [] };
            fs.readFile.mockResolvedValue(JSON.stringify(alugueis));

            const res = await verificaCiclistaComBicicleta('B1');
            expect(res).toBeNull();
        });
    });
});
