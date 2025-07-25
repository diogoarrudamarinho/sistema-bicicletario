const fs = require('fs/promises');
const path = require('path');

const dbFuncionario = path.join(__dirname, '../funcionarios.json');
async function buscaFuncionarioPorId(idFuncionario){
    try {
        const raw = await fs.readFile(dbFuncionario, 'utf-8');
        const funcionarios = JSON.parse(raw).funcionarios;

        const funcionario = funcionarios.find(c => c.id === idFuncionario);
        if (!funcionario) {
            throw new Error('Funcionário não encontrado');
        }

        return funcionario;
    } catch (error) {
        throw new Error('Erro ao buscar funcionário: ' + error.message);
    }
}

async function buscaFuncionarioPorEmail(email) {
    try {
        const raw = await fs.readFile(dbFuncionario, 'utf-8');
        const funcionarios = JSON.parse(raw).funcionarios;

        const funcionario = funcionarios.find(c => c.email === email);
        if (!funcionario) {
            throw new Error('Funcionário não encontrado');
        }

        return funcionario;
    } catch (error) {
        throw new Error('Erro ao buscar funcionário: ' + error.message);
    }
}

async function adicionaFuncionario(funcionario) {
    const { senha, email, nome, idade, funcao, cpf } = funcionario;
    try {
        if (!nome) throw new Error('Nome é obrigatório');
        if (!email) throw new Error('Email é obrigatório');
        if (!cpf) throw new Error('CPF é obrigatório');
        if (!senha) throw new Error('Senha é obrigatória');
        if (!funcao) throw new Error('Função é obrigatória');

        const raw = await fs.readFile(dbFuncionario, 'utf-8');
        const funcionariosJson = JSON.parse(raw);
        const funcionarios = funcionariosJson.funcionarios;

        // Verifica se o funcionário já existe
        const funcionarioExistente = funcionarios.find(f => f.email === email);
        if (funcionarioExistente) {
            throw new Error('Funcionário já cadastrado com esse email');
        }

        // Gera matrícula automática (ex: F001, F002...)
        const id = gerarIdSeguroNum();

        const numeroMatricula = (funcionarios.length + 1).toString().padStart(3, '0');
        const matricula = `F${numeroMatricula}`;

        // Cria novo funcionário
        const novoFuncionario = {
            id,
            matricula,
            senha,
            email,
            nome,
            idade,
            funcao,
            cpf
        };

        funcionarios.push(novoFuncionario);

        await fs.writeFile(
            dbFuncionario,
            JSON.stringify({ funcionarios }, null, 2)
        );

        return novoFuncionario;

    } catch (error) {
        throw new Error('Erro ao adicionar funcionário: ' + error.message);
    }
}
async function atualizaFuncionario(id, novosDados) {
    try {
        const raw = await fs.readFile(dbFuncionario, 'utf-8');
        const funcionarios = JSON.parse(raw).funcionarios;

        const funcionarioIndex = funcionarios.findIndex(f => f.id === id);
        if (funcionarioIndex === -1) {
            throw new Error('Funcionário não encontrado');
        }

        // Atualiza os dados do funcionário
        funcionarios[funcionarioIndex] = {
            ...funcionarios[funcionarioIndex],
            ...novosDados
        };

        await fs.writeFile(dbFuncionario, JSON.stringify({ funcionarios }, null, 2));

        return funcionarios[funcionarioIndex];
    } catch (error) {
        throw new Error('Erro ao atualizar funcionário: ' + error.message);
    }
}

async function deletaFuncionario(idFuncionario){
    try {
        const raw = await fs.readFile(dbFuncionario, 'utf-8');
        const funcionarios = JSON.parse(raw).funcionarios;

        const funcionarioIndex = funcionarios.findIndex(f => f.id === idFuncionario);
        if (funcionarioIndex === -1) {
            throw new Error('Funcionário não encontrado');
        }

        funcionarios.splice(funcionarioIndex, 1);

        await fs.writeFile(dbFuncionario, JSON.stringify({ funcionarios }, null, 2));

        return { mensagem: 'Funcionário deletado com sucesso' };
    } catch (error) {
        throw new Error('Erro ao deletar funcionário: ' + error.message);
    }
}

async function retornaTodosFuncionarios(){
    try {
        const raw = await fs.readFile(dbFuncionario, 'utf-8');
        return JSON.parse(raw).funcionarios;
    } catch (error) {
        throw new Error('Erro ao buscar funcionários: ' + error.message);
    }
}

module.exports = {
    buscaFuncionarioPorId,
    buscaFuncionarioPorEmail,
    adicionaFuncionario,
    atualizaFuncionario,
    deletaFuncionario,
    retornaTodosFuncionarios
}