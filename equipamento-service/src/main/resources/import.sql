INSERT INTO totens (id, localizacao, descricao) VALUES (1, 'Rio de Janeiro', 'Totem BD');

INSERT INTO bicicletas (id, marca, modelo, ano_fabricacao, status) VALUES  (1, 'Caloi', 'Caloi', 2020, 'DISPONIVEL'), (2, 'Caloi', 'Caloi', 2020, 'REPARO_SOLICITADO'), (3, 'Caloi', 'Caloi', 2020, 'EM_USO'), (4, 'Caloi', 'Caloi', 2020, 'EM_REPARO'), (5, 'Caloi', 'Caloi', 2020, 'DISPONIVEL');

INSERT INTO trancas (id, numero, modelo, ano_de_fabricacao, status, totem_id, bicicleta_id) VALUES  (1, 12345, 'Caloi', 2020, 'OCUPADA', 1, 1), (2, 12345, 'Caloi', 2020, 'LIVRE', 1, NULL), (3, 12345, 'Caloi', 2020, 'OCUPADA', 1, 2), (4, 12345, 'Caloi', 2020, 'OCUPADA', 1, 5), (5, 12345, 'Caloi', 2020, 'EM_REPARO', NULL, NULL), (6, 12345, 'Caloi', 2020, 'REPARO_SOLICITADO', 1, NULL);





