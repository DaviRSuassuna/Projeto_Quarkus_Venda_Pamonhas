-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO ingrediente (nome, precoUnitario, estoque, unidade_medida) VALUES
('Milho Verde', 3.50, 100, 'KG'),
('Acucar', 0.01, 50000, 'GRAMA'),
('Sal', 0.01, 30000, 'GRAMA'),
('Leite', 5.50, 200, 'LITRO'),
('Leite Condensado', 0.02, 5000, 'ML'),
('Queijo', 25.00, 15, 'KG'),
('Manteiga', 0.02, 8000, 'GRAMA'),
('Coco Ralado', 12.00, 8, 'KG'),
('Carne de Sol', 35.00, 5, 'KG'),
('Embalagem de Pamonha', 0.50, 200, 'UNIDADE');

INSERT INTO pamonha (nome, descricao, preco, estoque, sabor_pamonha, tipo_pamonha) VALUES
('Pamonha Doce', 'Pamonha tradicional doce', 8.50, 50, 'DOCE', 'TRADICIONAL'),
('Pamonha Salgada com Queijo', 'Pamonha salgada recheada com queijo', 10.00, 30, 'SALGADA', 'COM_QUEIJO'),
('Pamonha Gourmet Doce', 'Pamonha doce com receita especial', 12.50, 20, 'DOCE', 'GOURMET'),
('Pamonha Gourmet Salgada', 'Pamonha salgada com ingredientes selecionados', 13.00, 15, 'SALGADA', 'GOURMET'),
('Pamonha Tradicional Salgada', 'Pamonha simples salgada', 9.00, 40, 'SALGADA', 'TRADICIONAL');