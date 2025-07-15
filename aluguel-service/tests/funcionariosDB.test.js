const fs = require('fs/promises');
jest.mock('fs/promises');

const funcionariosDB = require('../repositories/acessoDB/funcionariosDB.js');

const mockFuncionarios = {
  funcionarios: [
    {
      id: "1",
      matricula: "F001",
      nome: "Ana Souza",
      email: "ana@email.com",
      cpf: "12345678900",
      idade: 30,
      funcao: "Atendente",
      senha: "senha123"
    }
  ]
};

describe('funcionariosDB', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('busca funcionário por ID', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));

    const result = await funcionariosDB.buscaFuncionarioPorId("F001");
    expect(result.nome).toBe("Ana Souza");
  });

  it('busca funcionário por email', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));

    const result = await funcionariosDB.buscaFuncionarioPorEmail("ana@email.com");
    expect(result.funcao).toBe("Atendente");
  });

  it('retorna erro se funcionário não encontrado por ID', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));

    await expect(funcionariosDB.buscaFuncionarioPorId("999"))
      .rejects.toThrow('Funcionário não encontrado');
  });

  it('adiciona novo funcionário', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));
    fs.writeFile.mockResolvedValue();

    const novo = await funcionariosDB.adicionaFuncionario({
      senha: "novaSenha",
      email: "novo@email.com",
      nome: "Carlos",
      idade: 28,
      funcao: "Supervisor",
      cpf: "11122233344"
    });

    expect(novo.nome).toBe("Carlos");
    expect(fs.writeFile).toHaveBeenCalled();
  });

  it('não adiciona funcionário duplicado por email', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));

    await expect(
      funcionariosDB.adicionaFuncionario({
        senha: "senha123",
        email: "ana@email.com", // já existente
        nome: "Ana",
        idade: 30,
        funcao: "Atendente",
        cpf: "12345678900"
      })
    ).rejects.toThrow('Funcionário já cadastrado com esse email');
  });

  it('deve lançar erro se nome não for informado', async () => {
    await expect(
      funcionariosDB.adicionaFuncionario({
        senha: "senha123",
        email: "email@teste.com",
        nome: "",       // nome vazio
        idade: 25,
        funcao: "Atendente",
        cpf: "12345678900"
      })
    ).rejects.toThrow('Nome é obrigatório');
  });

  it('deve lançar erro se email não for informado', async () => {
    await expect(
      funcionariosDB.adicionaFuncionario({
        senha: "senha123",
        email: "",        // email vazio
        nome: "José",
        idade: 25,
        funcao: "Atendente",
        cpf: "12345678900"
      })
    ).rejects.toThrow('Email é obrigatório');
  });

  it('deve lançar erro se cpf não for informado', async () => {
    await expect(
      funcionariosDB.adicionaFuncionario({
        senha: "senha123",
        email: "email@teste.com",
        nome: "José",
        idade: 25,
        funcao: "Atendente",
        cpf: ""        // cpf vazio
      })
    ).rejects.toThrow('CPF é obrigatório');
  });

  it('deve lançar erro se senha não for informada', async () => {
    await expect(
      funcionariosDB.adicionaFuncionario({
        senha: "",        // senha vazia
        email: "email@teste.com",
        nome: "José",
        idade: 25,
        funcao: "Atendente",
        cpf: "12345678900"
      })
    ).rejects.toThrow('Senha é obrigatória');
  });

  it('deve lançar erro se função não for informada', async () => {
    await expect(
      funcionariosDB.adicionaFuncionario({
        senha: "senha123",
        email: "email@teste.com",
        nome: "José",
        idade: 25,
        funcao: "",        // função vazia
        cpf: "12345678900"
      })
    ).rejects.toThrow('Função é obrigatória');
  });

  it('atualiza funcionário existente', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));
    fs.writeFile.mockResolvedValue();

    const atualizado = await funcionariosDB.atualizaFuncionario("F001", { nome: "Ana Atualizada" });

    expect(atualizado.nome).toBe("Ana Atualizada");
    expect(fs.writeFile).toHaveBeenCalled();
  });

  it('deleta funcionário existente', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));
    fs.writeFile.mockResolvedValue();

    const result = await funcionariosDB.deletaFuncionario("F001");
    expect(result.mensagem).toBe('Funcionário deletado com sucesso');
    expect(fs.writeFile).toHaveBeenCalled();
  });

  it('retorna todos os funcionários', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockFuncionarios));

    const lista = await funcionariosDB.retornaTodosFuncionarios();
    expect(lista.length).toBe(1);
    expect(lista[0].nome).toBe("Ana Souza");
  });
});
