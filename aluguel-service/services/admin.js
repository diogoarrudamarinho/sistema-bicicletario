const { resetarTodosArquivos } = require('../repositories/acessoDB/adminDB');

async function limparBaseDeDados() {
  await resetarTodosArquivos();
}

module.exports = { limparBaseDeDados };