-- Inserindo Totens
INSERT INTO totem (localizacao, descricao) VALUES ('Rio de Janeiro', 'Estação Central');

-- Inserindo Bicicletas
INSERT INTO bicicleta (marca, modelo, ano, numero, status) VALUES ('Caloi', 'Caloi', '2020', 12345, 'DISPONIVEL');
INSERT INTO bicicleta (marca, modelo, ano, numero, status) VALUES ('Caloi', 'Caloi', '2020', 12346, 'REPARO_SOLICITADO');
INSERT INTO bicicleta (marca, modelo, ano, numero, status) VALUES ('Caloi', 'Caloi', '2020', 12347, 'EM_USO');
INSERT INTO bicicleta (marca, modelo, ano, numero, status) VALUES ('Caloi', 'Caloi', '2020', 12348, 'EM_REPARO');
INSERT INTO bicicleta (marca, modelo, ano, numero, status) VALUES ('Caloi', 'Caloi', '2020', 12349, 'DISPONIVEL');

-- Inserindo Trancas (referenciando Totem 1 e as Bicicletas)
-- Tranca 1: Ocupada com Bike 1
INSERT INTO tranca (numero, localizacao, ano_de_fabricacao, modelo, status, totem_id, bicicleta_id) 
VALUES (12345, 'Rio de Janeiro', '2020', 'Caloi', 'OCUPADA', 1, 1);

-- Tranca 2: Livre (Sem bike)
INSERT INTO tranca (numero, localizacao, ano_de_fabricacao, modelo, status, totem_id, bicicleta_id) 
VALUES (12346, 'Rio de Janeiro', '2020', 'Caloi', 'LIVRE', 1, NULL);

-- Tranca 3: Ocupada com Bike 2
INSERT INTO tranca (numero, localizacao, ano_de_fabricacao, modelo, status, totem_id, bicicleta_id) 
VALUES (12347, 'Rio de Janeiro', '2020', 'Caloi', 'OCUPADA', 1, 2);

-- Tranca 4: Ocupada com Bike 5
INSERT INTO tranca (numero, localizacao, ano_de_fabricacao, modelo, status, totem_id, bicicleta_id) 
VALUES (12348, 'Rio de Janeiro', '2020', 'Caloi', 'OCUPADA', 1, 5);

-- Tranca 5: Em reparo (Sem totem/bike no exemplo)
INSERT INTO tranca (numero, localizacao, ano_de_fabricacao, modelo, status, totem_id, bicicleta_id) 
VALUES (12349, 'Rio de Janeiro', '2020', 'Caloi', 'EM_REPARO', NULL, NULL);

-- Tranca 6: Reparo Solicitado
INSERT INTO tranca (numero, localizacao, ano_de_fabricacao, modelo, status, totem_id, bicicleta_id) 
VALUES (12350, 'Rio de Janeiro', '2020', 'Caloi', 'REPARO_SOLICITADO', 1, NULL);