

// Função simples de validação de CPF - só para ver se tem 11 dígitos
function validarCPF(cpf) {
    return /^\d{11}$/.test(cpf);
}

// Função simples de validação de e-mail
function validarEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

module.exports = {
    validarCPF,
    validarEmail
}