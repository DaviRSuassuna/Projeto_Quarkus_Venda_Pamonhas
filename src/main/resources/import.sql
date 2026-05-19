-- Import inicial de dados
INSERT INTO usuario (login, senha, perfil, data_cadastro) VALUES
('admin', '$2a$10$TkB2wCdCdgmiy.Z/q3GSIuOT7QEiSk4kzKYxvzk8UC2CO2TgF3CMe', 'ROLE_ADMIN', NOW());
-- Data inicial adaptada ao modelo atual do projeto.

INSERT INTO ingrediente (id, nome, preco_unitario, estoque, unidade_medida) VALUES
(1, 'Milho Verde', 3.50, 100, 'KG'),
(2, 'Açúcar', 0.01, 50000, 'GRAMA'),
(3, 'Sal', 0.01, 30000, 'GRAMA'),
(4, 'Leite', 5.50, 200, 'LITRO'),
(5, 'Queijo', 25.00, 15, 'KG');

INSERT INTO categoria (id, nome, descricao) VALUES
(1, 'Tradicional', 'Pamonhas com receitas tradicionais e simples'),
(2, 'Especial', 'Pamonhas gourmet e recheadas'),
(3, 'Doces', 'Pamonhas com sabor doce e sobremesas');

INSERT INTO modo_preparo (id, descricao, tempo_preparo_minutos) VALUES
(1, 'Cozinhar no vapor por 40 minutos', 40),
(2, 'Assar em forno médio por 30 minutos', 30);

INSERT INTO embalagem (id, descricao, custo, peso_suportado) VALUES
(1, 'Embalagem plástica simples', 0.50, 0.5),
(2, 'Embalagem biodegradável premium', 1.20, 0.7);

INSERT INTO embalagem_plastica (id, tipo_plastico, reciclavel) VALUES
(1, 'Polietileno', true);

INSERT INTO embalagem_biodegradavel (id, material, tempo_decomposicao_dias) VALUES
(2, 'Papel vegetal', 90);

INSERT INTO pamonha (
    id, nome, descricao, preco, estoque,
    modo_preparo_id, embalagem_id,
    valor_energetico, carboidratos, proteinas, gorduras_totais, fibras, sodio
) VALUES
(1, 'Pamonha Doce Tradicional', 'Pamonha doce com massa de milho e açúcar', 8.50, 50, 1, 1, 360, 80, 9, 4, 7, 2),
(2, 'Pamonha Salgada com Queijo', 'Pamonha salgada com queijo e temperos', 10.00, 30, 2, 2, 250, 15, 26, 6, 0, 900);

INSERT INTO item_receita (id, quantidade, unidade_medida, pamonha_id, ingrediente_id) VALUES
(1, 2, 'KG', 1, 1),
(2, 500, 'GRAMA', 1, 2),
(3, 200, 'MILILITRO', 2, 4),
(4, 300, 'GRAMA', 2, 5);

INSERT INTO pamonha_categoria (pamonha_id, categoria_id) VALUES
(1, 3),
(2, 2);