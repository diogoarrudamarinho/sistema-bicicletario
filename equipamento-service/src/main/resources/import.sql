INSERT INTO totens (localizacao, descricao) VALUES ('Rio de Janeiro', 'Totem BD');

INSERT INTO bicicletas (marca, modelo, ano_fabricacao, status) VALUES  ('Caloi', 'Caloi', 2020, 'DISPONIVEL'), ('Caloi', 'Caloi', 2020, 'REPARO_SOLICITADO'), ('Caloi', 'Caloi', 2020, 'EM_USO'), ('Caloi', 'Caloi', 2020, 'EM_REPARO'), ('Caloi', 'Caloi', 2020, 'DISPONIVEL');

INSERT INTO trancas (numero, modelo, ano_de_fabricacao, status, totem_id, bicicleta_id) VALUES  (12345, 'Caloi', 2020, 'OCUPADA', 1, 1), (12345, 'Caloi', 2020, 'LIVRE', 1, NULL), (12345, 'Caloi', 2020, 'OCUPADA', 1, 2), (12345, 'Caloi', 2020, 'OCUPADA', 1, 5), (12345, 'Caloi', 2020, 'EM_REPARO', NULL, NULL), (12345, 'Caloi', 2020, 'REPARO_SOLICITADO', 1, NULL);

UPDATE trancas SET bicicleta_id = 1 WHERE id = 1;
UPDATE trancas SET bicicleta_id = 2 WHERE id = 3;
UPDATE trancas SET bicicleta_id = 5 WHERE id = 4;




