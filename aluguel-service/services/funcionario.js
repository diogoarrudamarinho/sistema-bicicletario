const database = require('../repositories/acessoDB/funcionariosDB');
async function criaFuncionario(funcionario) {
    const funcionarioExistente = await database.buscaFuncionarioPorEmail(funcionario.email);
    if (funcionarioExistente) {
        throw new Error('Funcionário já cadastrado com esse email');
    }

    await database.adicionaFuncionario(funcionario);
    return retornaTodosFuncionarios();
}

async function atualizaFuncionario(id, dados) {
    const resultado = await database.atualizaFuncionario(id, dados);

    if (!resultado) {
        throw new Error('Funcionário não encontrado');
    }

    return retornaTodosFuncionarios();
}

async function deletaFuncionario(id) {
    const resultado = await database.deletaFuncionario(id);
    if (!resultado) {
        throw new Error('Erro ao deletar funcionário');
    }
    return retornaTodosFuncionarios();
}

async function retornaTodosFuncionarios(){
    const funcionarios = await database.retornaTodosFuncionarios();
    if (!funcionarios) {
        throw new Error('Erro ao buscar funcionários');
    }
    return funcionarios;
}

async function buscaFuncionarioPorId(id){
    const funcionario = await database.buscaFuncionarioPorId(id);
    if (!funcionario) {
        throw new Error('Funcionário não encontrado');
    }
    return funcionario;
}

module.exports = {
    criaFuncionario,
    atualizaFuncionario,
    deletaFuncionario,
    retornaTodosFuncionarios,
    buscaFuncionarioPorId
};


