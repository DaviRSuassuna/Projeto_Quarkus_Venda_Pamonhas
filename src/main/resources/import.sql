-- Import inicial de dados
INSERT INTO usuario (id, email, senha, data_cadastro) VALUES
(1, 'admin@pamonhas.com', '$2a$10$P0..VOZ8wzas1xrY7wPcEOhNi8gMhURNJKSQrge/7zzhLyaPRKh1i', NOW());

INSERT INTO usuario_perfis (usuario_id, perfil) VALUES
(1, 'ROLE_ADMIN');
-- Data inicial adaptada ao modelo atual do projeto.
-- Senha: pamonha123

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

INSERT INTO cupom_desconto (id, codigo, descricao, valor_desconto, data_validade, data_cadastro) VALUES
(1, 'BEMVINDO5',  'Desconto de R$ 5 para novos clientes',          5.00, '2026-12-31', NOW()),
(2, 'PAMONHA10', 'Desconto de R$ 10 em pamonhas selecionadas',    10.00, '2026-12-31', NOW()),
(3, 'DOCE3',     'R$ 3 de desconto na Pamonha Doce',               3.00, '2026-12-31', NOW());

INSERT INTO cupom_pamonha (cupom_id, pamonha_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2),
(3, 1);

INSERT INTO estado (id, nome, sigla, data_cadastro) VALUES
(1,  'Acre',                 'AC', NOW()),
(2,  'Alagoas',              'AL', NOW()),
(3,  'Amazonas',             'AM', NOW()),
(4,  'Amapá',                'AP', NOW()),
(5,  'Bahia',                'BA', NOW()),
(6,  'Ceará',                'CE', NOW()),
(7,  'Distrito Federal',     'DF', NOW()),
(8,  'Espírito Santo',       'ES', NOW()),
(9,  'Goiás',                'GO', NOW()),
(10, 'Maranhão',             'MA', NOW()),
(11, 'Minas Gerais',         'MG', NOW()),
(12, 'Mato Grosso do Sul',   'MS', NOW()),
(13, 'Mato Grosso',          'MT', NOW()),
(14, 'Pará',                 'PA', NOW()),
(15, 'Paraíba',              'PB', NOW()),
(16, 'Pernambuco',           'PE', NOW()),
(17, 'Piauí',                'PI', NOW()),
(18, 'Paraná',               'PR', NOW()),
(19, 'Rio de Janeiro',       'RJ', NOW()),
(20, 'Rio Grande do Norte',  'RN', NOW()),
(21, 'Rondônia',             'RO', NOW()),
(22, 'Roraima',              'RR', NOW()),
(23, 'Rio Grande do Sul',    'RS', NOW()),
(24, 'Santa Catarina',       'SC', NOW()),
(25, 'Sergipe',              'SE', NOW()),
(26, 'São Paulo',            'SP', NOW()),
(27, 'Tocantins',            'TO', NOW());

INSERT INTO cidade (id, nome, estado_id, data_cadastro) VALUES
(1,  'Rio Branco',      1,  NOW()),
(2,  'Maceió',          2,  NOW()),
(3,  'Manaus',          3,  NOW()),
(4,  'Macapá',          4,  NOW()),
(5,  'Salvador',        5,  NOW()),
(6,  'Fortaleza',       6,  NOW()),
(7,  'Brasília',        7,  NOW()),
(8,  'Vitória',         8,  NOW()),
(9,  'Goiânia',         9,  NOW()),
(10, 'São Luís',        10, NOW()),
(11, 'Belo Horizonte',  11, NOW()),
(12, 'Campo Grande',    12, NOW()),
(13, 'Cuiabá',          13, NOW()),
(14, 'Belém',           14, NOW()),
(15, 'João Pessoa',     15, NOW()),
(16, 'Recife',          16, NOW()),
(17, 'Teresina',        17, NOW()),
(18, 'Curitiba',        18, NOW()),
(19, 'Rio de Janeiro',  19, NOW()),
(20, 'Natal',           20, NOW()),
(21, 'Porto Velho',     21, NOW()),
(22, 'Boa Vista',       22, NOW()),
(23, 'Porto Alegre',    23, NOW()),
(24, 'Florianópolis',   24, NOW()),
(25, 'Aracaju',         25, NOW()),
(26, 'São Paulo',       26, NOW()),
(27, 'Palmas',          27, NOW());

-- Reinicia as sequences para evitar conflito de chave primária após inserts com IDs explícitos
ALTER TABLE usuario ALTER COLUMN id RESTART WITH 2;
ALTER TABLE ingrediente ALTER COLUMN id RESTART WITH 6;
ALTER TABLE categoria ALTER COLUMN id RESTART WITH 4;
ALTER TABLE modo_preparo ALTER COLUMN id RESTART WITH 3;
ALTER TABLE embalagem ALTER COLUMN id RESTART WITH 3;
ALTER TABLE pamonha ALTER COLUMN id RESTART WITH 3;
ALTER TABLE item_receita ALTER COLUMN id RESTART WITH 5;
ALTER TABLE cupom_desconto ALTER COLUMN id RESTART WITH 4;
ALTER TABLE estado ALTER COLUMN id RESTART WITH 28;
ALTER TABLE cidade ALTER COLUMN id RESTART WITH 28;