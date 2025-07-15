const database = require('../repositories/acessoDB/funcionariosDB');
const funcionarioService = require('../services/funcionario');
const { beforeEach, describe, it, expect } = require('@jest/globals');

// Mocks
jest.mock('../repositories/acessoDB/funcionariosDB');

describe('funcionarioService', () => {
    beforeEach(() => jest.clearAllMocks());

    describe('criaFuncionario', () => {
        const newFunc = { email: 'test@example.com', senha: '123', nome: 'Test', idade: 30, funcao: 'dev', cpf: '12345678900' };

        it('deve lançar erro se funcionário já existir', async () => {
            database.buscaFuncionarioPorEmail.mockResolvedValue({ id: '1', email: newFunc.email });
            await expect(funcionarioService.criaFuncionario(newFunc))
                .rejects.toThrow('Funcionário já cadastrado com esse email');
            expect(database.buscaFuncionarioPorEmail).toHaveBeenCalledWith(newFunc.email);
        });

        it('deve chamar database.adicionaFuncionario em caso de sucesso', async () => {
            database.buscaFuncionarioPorEmail.mockResolvedValue(null);
            database.adicionaFuncionario.mockResolvedValue({ id: '2', ...newFunc });

            const result = await funcionarioService.criaFuncionario(newFunc);
            expect(database.adicionaFuncionario).toHaveBeenCalledWith(newFunc);
            expect(result).toEqual({ id: '2', ...newFunc });
        });
    });

    describe('atualizaFuncionario', () => {
        const id = '1';
        const dados = { nome: 'Updated' };

        it('deve lançar erro se não encontrar funcionário', async () => {
            database.atualizaFuncionario.mockResolvedValue(null);
            await expect(funcionarioService.atualizaFuncionario(id, dados))
                .rejects.toThrow('Funcionário não encontrado');
            expect(database.atualizaFuncionario).toHaveBeenCalledWith(id, dados);
        });

        it('deve retornar resultado em caso de sucesso', async () => {
            const updated = { id, ...dados };
            database.atualizaFuncionario.mockResolvedValue(updated);
            const result = await funcionarioService.atualizaFuncionario(id, dados);
            expect(result).toEqual(updated);
        });
    });

    describe('deletaFuncionario', () => {
        const id = '1';

        it('deve lançar erro se falhar ao deletar', async () => {
            database.deletaFuncionario.mockResolvedValue(null);
            await expect(funcionarioService.deletaFuncionario(id))
                .rejects.toThrow('Erro ao deletar funcionário');
            expect(database.deletaFuncionario).toHaveBeenCalledWith(id);
        });

        it('deve retornar mensagem de sucesso', async () => {
            database.deletaFuncionario.mockResolvedValue(true);
            const result = await funcionarioService.deletaFuncionario(id);
            expect(result).toEqual({ mensagem: 'Funcionário deletado com sucesso' });
        });
    });

    describe('retornaTodosFuncionarios', () => {
        it('deve lançar erro se falhar ao buscar', async () => {
            database.retornaTodosFuncionarios.mockResolvedValue(null);
            await expect(funcionarioService.retornaTodosFuncionarios())
                .rejects.toThrow('Erro ao buscar funcionários');
        });

        it('deve retornar lista de funcionários', async () => {
            const list = [{ id: '1' }, { id: '2' }];
            database.retornaTodosFuncionarios.mockResolvedValue(list);
            const result = await funcionarioService.retornaTodosFuncionarios();
            expect(result).toBe(list);
        });
    });

    describe('buscaFuncionarioPorId', () => {
        const id = '1';

        it('deve lançar erro se não encontrar', async () => {
            database.buscaFuncionarioPorId.mockResolvedValue(null);
            await expect(funcionarioService.buscaFuncionarioPorId(id))
                .rejects.toThrow('Funcionário não encontrado');
            expect(database.buscaFuncionarioPorId).toHaveBeenCalledWith(id);
        });

        it('deve retornar funcionário encontrado', async () => {
            const func = { id, nome: 'Test' };
            database.buscaFuncionarioPorId.mockResolvedValue(func);
            const result = await funcionarioService.buscaFuncionarioPorId(id);
            expect(result).toEqual(func);
        });
    });
});