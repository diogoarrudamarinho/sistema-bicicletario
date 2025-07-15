const fs = require('fs/promises');
const path = require('path');

// Mockando o fs para controlar leitura e escrita
jest.mock('fs/promises');

// Mock do crypto.randomBytes para gerar IDs previsíveis
jest.mock('crypto', () => ({
  randomBytes: jest.fn(() => Buffer.from([0, 0, 39, 16])),
}));

const dbPath = path.join(__dirname, '../repositories/data/ciclistas.json');

const ciclistaDB = require('../repositories/acessoDB/ciclistaDB.js');

const mockDB = {
  ciclistas: [
    {
      id: "10001",
      nome: "João da Silva",
      email: "joao@email.com",
      cpf: "12345678900",
      ativado: false
    },
    {
      id: "10002",
      nome: "Maria Souza",
      email: "maria@email.com",
      cpf: "98765432100",
      ativado: true
    }
  ],
  meiosPagamento: [
    {
      id: "20001",
      ciclistaId: "10001",
      numero_cartao: "4111111111111111",
      nome_no_cartao: "João da Silva",
      validade: "12/25",
      codigo_seg: "123"
    },
    {
      id: "20002",
      ciclistaId: "10002",
      numero_cartao: "5500000000000004",
      nome_no_cartao: "Maria Souza",
      validade: "11/24",
      codigo_seg: "456"
    }
  ]
};

describe('ciclistaDB', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve ler o DB e retornar o ciclista pelo id', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));

    const ciclista = await ciclistaDB.obterCiclista('10001');

    expect(fs.readFile).toHaveBeenCalledWith(dbPath, 'utf-8');
    expect(ciclista).toBeDefined();
    expect(ciclista.nome).toBe('João da Silva');
    expect(ciclista.meioDePagamento.numero_cartao).toBe('4111111111111111');
  });

  it('deve lançar erro ao não encontrar ciclista', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));

    await expect(ciclistaDB.obterCiclista('99999')).rejects.toThrow('Ciclista não encontrado');
  });

  it('deve criar um novo ciclista e salvar no DB', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));
    fs.writeFile.mockResolvedValue();

    const novoCiclista = {
      nome: 'Carlos Pereira',
      email: 'carlos@email.com',
      cpf: '11122233344'
    };
    const meioDePagamento = {
      numero_cartao: '4000000000000000',
      nome_no_cartao: 'Carlos Pereira',
      validade: '01/28',
      codigo_seg: '789'
    };

    const result = await ciclistaDB.criarCiclista(novoCiclista, meioDePagamento);

    expect(result).toHaveProperty('id', '10000'); // id gerado com base no buffer mockado
    expect(result.nome).toBe('Carlos Pereira');
    expect(fs.writeFile).toHaveBeenCalledWith(
      dbPath,
      expect.stringContaining('Carlos Pereira'),
      'utf-8'
    );
  });

  it('deve atualizar dados do ciclista', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));
    fs.writeFile.mockResolvedValue();

    const dadosParaAtualizar = { nome: 'João Silva Atualizado' };
    const ciclistaAtualizado = await ciclistaDB.atualizarCiclista('10001', dadosParaAtualizar);

    expect(ciclistaAtualizado.nome).toBe('João Silva Atualizado');
    expect(fs.writeFile).toHaveBeenCalled();
  });

  it('deve lançar erro ao tentar atualizar ciclista inexistente', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));
    await expect(ciclistaDB.atualizarCiclista('99999', { nome: 'Outro' })).rejects.toThrow('Ciclista não encontrado');
  });

  it('deve ativar ciclista', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));
    fs.writeFile.mockResolvedValue();

    const ativado = await ciclistaDB.ativarCiclista('10001');

    expect(ativado.ativado).toBe(true);
    expect(fs.writeFile).toHaveBeenCalled();
  });

  it('deve verificar existência de email', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));

    const existe = await ciclistaDB.existeEmail('joao@email.com');
    expect(existe).toBe(true);

    const naoExiste = await ciclistaDB.existeEmail('email@naoexiste.com');
    expect(naoExiste).toBe(false);
  });

  it('deve deletar ciclista e seus meios de pagamento', async () => {
    fs.readFile.mockResolvedValue(JSON.stringify(mockDB));
    fs.writeFile.mockResolvedValue();

    const resultado = await ciclistaDB.deletarCiclista('10001');

    expect(resultado).toHaveProperty('mensagem', 'Ciclista removido com sucesso');
    expect(fs.writeFile).toHaveBeenCalled();
  });
});
