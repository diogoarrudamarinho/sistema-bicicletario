-- Inserindo Ciclistas (Nacionalidade e Status são Enums/Strings)
INSERT INTO ciclista (nome, nascimento, cpf, nacionalidade, email, senha, status, numero, validade, pais) 
VALUES ('Fulano Beltrano', '2021-05-02', '78804034009', 'BRASILEIRO', 'user@example.com', 'ABC123', 'ATIVO', NULL, NULL, NULL);

INSERT INTO ciclista (nome, nascimento, cpf, nacionalidade, email, senha, status, numero, validade, pais) 
VALUES ('Ciclista 2', '2021-05-02', '43943488039', 'BRASILEIRO', 'user2@example.com', 'ABC123', 'ATIVO', NULL, NULL, NULL);

INSERT INTO ciclista (nome, nascimento, cpf, nacionalidade, email, senha, status, numero, validade, pais) 
VALUES ('Ciclista 3', '2021-05-02', '10243164084', 'BRASILEIRO', 'user3@example.com', 'ABC123', 'ATIVO', NULL, NULL, NULL);

INSERT INTO ciclista (nome, nascimento, cpf, nacionalidade, email, senha, status, numero, validade, pais) 
VALUES ('Ciclista 4', '2021-05-02', '30880150017', 'BRASILEIRO', 'user4@example.com', 'ABC123', 'ATIVO', NULL, NULL, NULL);

-- Inserindo Funcionário
INSERT INTO funcionario (senha, confirmacao_senha, email, nome, idade, funcao, cpf) 
VALUES ('123', '123', 'employee@example.com', 'Beltrano', '25', 'Reparador', '99999999999');