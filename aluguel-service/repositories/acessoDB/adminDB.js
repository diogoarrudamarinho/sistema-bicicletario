const fs = require('fs/promises');
const path = require('path');

const arquivos = {
  ciclistas: {
    path: path.join(__dirname, '../ciclistas.json'),
    conteudoVazio: {
      ciclistas: [],
      meiosPagamento: []
    }
  },
  alugueis: {
    path: path.join(__dirname, '../alugueis.json'),
    conteudoVazio: {
      alugueis: []
    }
  },
  funcionarios: {
    path: path.join(__dirname, '../funcionarios.json'),
    conteudoVazio: {
      funcionarios: []
    }
  }
};

async function resetarTodosArquivos() {
  const erros = [];

  for (const nome in arquivos) {
    const { path: filePath, conteudoVazio } = arquivos[nome];
    try {
      await fs.writeFile(filePath, JSON.stringify(conteudoVazio, null, 2), 'utf-8');
    } catch (err) {
      erros.push({ arquivo: nome, erro: err.message });
    }
  }

  if (erros.length > 0) {
    throw new Error(`Falha ao limpar arquivos: ${JSON.stringify(erros)}`);
  }
}

module.exports = { resetarTodosArquivos };