-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO ingrediente (
    id, nome, preco_unitario, estoque, unidade_medida,
    valor_energetico, carboidratos, acucares_totais, proteinas,
    gorduras_total, gorduras_saturadas, gorduras_trans,
    fibras_alimentares, sodio
) VALUES
(1,'Milho Verde', 3.50, 100, 'KG', 360, 80, 5, 9, 4, 1, 0, 7, 2),
(2,'Acucar', 0.01, 50000, 'GRAMA', 400, 100, 100, 0, 0, 0, 0, 0, 1),
(3,'Sal', 0.01, 30000, 'GRAMA', 0, 0, 0, 0, 0, 0, 0, 0, 38700),
(4,'Leite', 5.50, 200, 'LITRO', 60, 5, 5, 3, 3, 2, 0, 0, 50),
(5,'Leite Condensado', 0.02, 5000, 'ML', 320, 55, 55, 8, 8, 5, 0, 0, 120),
(6,'Queijo', 25.00, 15, 'KG', 400, 2, 1, 25, 33, 20, 1, 0, 600),
(7,'Manteiga', 0.02, 8000, 'GRAMA', 720, 0, 0, 1, 80, 50, 3, 0, 700),
(8,'Coco Ralado', 12.00, 8, 'KG', 660, 24, 6, 7, 65, 57, 0, 16, 37),
(9,'Carne de Sol', 35.00, 5, 'KG', 250, 0, 0, 26, 15, 6, 0, 0, 900),
(10,'Embalagem de Pamonha', 0.50, 200, 'UNIDADE', 0, 0, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO receita (id, descricao) VALUES
(1, 'Receita doce tradicional'),
(2, 'Receita salgada com queijo');

INSERT INTO item_receita (id, quantidade, unidade_medida, receita_id, ingrediente_id) VALUES
(1, 2, 'KG', 1, 1),
(2, 500, 'GRAMA', 1, 2),
(3, 200, 'ML', 1, 5),
(4, 2, 'KG', 2, 1),
(5, 300, 'GRAMA', 2, 6),
(6, 50, 'GRAMA', 2, 3);

INSERT INTO pamonha (id, nome, descricao, preco, estoque, sabor_pamonha, tipo_pamonha, receita_id) VALUES
(1,'Pamonha Doce', 'Pamonha tradicional doce', 8.50, 50, 'DOCE', 'TRADICIONAL', 1),
(2,'Pamonha Salgada com Queijo', 'Pamonha salgada recheada com queijo', 10.00, 30, 'SALGADA', 'COM_QUEIJO', 2);

INSERT INTO cupom_desconto (id, codigo, valor_desconto, data_validade, ativo)
VALUES 
(1, 'DESC10', 10.00, '2026-12-31', true),
(2, 'DESC20', 20.00, '2026-10-15', true),
(3, 'PROMO5', 5.50, '2026-08-01', true),
(4, 'OLD15', 15.00, '2024-01-01', false),
(5, 'BLACK50', 50.00, '2025-11-25', false),
(6, 'SUPER30', 30.00, '2026-09-10', true),
(7, 'MINI2', 2.00, '2026-07-01', true);