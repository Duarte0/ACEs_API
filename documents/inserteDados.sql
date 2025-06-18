-- Inserções para a tabela Area
INSERT INTO Area (nome, descricao, regiao, populacaoAprox, nivelRisco, prioridade, dataUltimaAtt)
VALUES ('Setor Central', 'Área urbana densamente povoada com muitos prédios e residências.', 'Central', 15000, 'MEDIO',
        'ALTO', '2025-06-15 10:00:00'),
       ('Vila Nova', 'Região residencial com muitas casas e quintais grandes, histórico de focos.', 'Leste', 8000,
        'ALTO', 'MUITO_ALTO', '2025-06-16 14:30:00'),
       ('Jardim América', 'Bairro de classe média com poucas áreas verdes, próximo a córregos.', 'Sul', 12000, 'MEDIO',
        'MEDIO', '2025-06-14 09:15:00'),
       ('Parque Industrial', 'Área com grandes galpões e poucas residências, risco de focos em depósitos.', 'Norte',
        3000, 'BAIXO', 'BAIXO', '2025-06-13 11:00:00'),
       ('Morada do Sol', 'Bairro periférico com infraestrutura variada, necessidade de atenção redobrada.', 'Oeste',
        10000, 'MUITO_ALTO', 'MUITO_ALTO', '2025-06-17 08:45:00');

-- Inserções para a tabela Endereco (assumindo IDs de Area de 1 a 5)
INSERT INTO Endereco (area_id, logradouro, numero, complemento, bairro, cep, latitude, longitude, spolmovel)
VALUES (1, 'Rua das Flores', '123', 'Casa A', 'Setor Central', '75901-001', -17.801234, -51.805678, FALSE),
       (1, 'Av. Brasil', '456', 'Apto 101', 'Setor Central', '75901-002', -17.800500, -51.806000, FALSE),
       (2, 'Rua do Bosque', '789', NULL, 'Vila Nova', '75902-001', -17.810100, -51.790500, FALSE),
       (2, 'Travessa da Paz', '10', 'Fundos', 'Vila Nova', '75902-002', -17.811500, -51.791000, FALSE),
       (3, 'Alameda dos Ipês', '321', NULL, 'Jardim América', '75903-001', -17.795000, -51.815000, FALSE),
       (4, 'Rodovia BR-060', 'SN', 'Galpão 5', 'Parque Industrial', '75904-001', -17.780000, -51.820000, FALSE),
       (5, 'Rua Boa Vista', '55', NULL, 'Morada do Sol', '75905-001', -17.820000, -51.780000, TRUE);

-- Inserções para a tabela Paciente (assumindo IDs de Endereco de 1 a 7)
INSERT INTO Paciente (endereco_id, nome, dataNascimento, sexo, cpf, cartaoSUS, telefone)
VALUES (1, 'Maria Oliveira', '1985-03-20', 'FEMININO', '111.222.333-44', '1234567890123456', '(64) 9999-8888'),
       (2, 'Carlos Pereira', '1990-07-10', 'MASCULINO', '222.333.444-55', '2345678901234567', '(64) 9888-7777'),
       (3, 'Ana Souza', '1975-11-25', 'FEMININO', '333.444.555-66', '3456789012345678', '(64) 9777-6666'),
       (4, 'Pedro Almeida', '2000-01-01', 'MASCULINO', '444.555.666-77', '4567890123456789', '(64) 9666-5555'),
       (7, 'Juliana Santos', '1995-09-12', 'FEMININO', '555.666.777-88', '5678901234567890', '(64) 9555-4444');

-- Inserções para a tabela Agente
INSERT INTO Agente (nome, matricula, dataAdmissao, dataInicio, dataFim, ativo)
VALUES ('Fernanda Costa', 'AG001', '2022-01-01', '2022-01-01', NULL, TRUE),
       ('Ricardo Lima', 'AG002', '2021-05-10', '2021-05-10', NULL, TRUE),
       ('Patrícia Mendes', 'AG003', '2023-03-15', '2023-03-15', '2025-12-31', TRUE),
       ('Gustavo Rocha', 'AG004', '2020-08-01', '2020-08-01', NULL, TRUE);

-- Inserções para a tabela AgenteArea (assumindo IDs de Agente de 1 a 4 e IDs de Area de 1 a 5)
INSERT INTO AgenteArea (agente_id, area_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3),
       (4, 4),
       (4, 5);

-- Inserções para a tabela Visita (assumindo IDs de Agente de 1 a 4 e IDs de Endereco de 1 a 7)
-- Os IDs dessas visitas são cruciais para as próximas tabelas (FocoAedes, CasoDengue)
INSERT INTO Visita (agente_id, endereco_id, dataHora, observacoes, status, temperatura, foiRealizada)
VALUES (1, 1, '2025-06-17 10:00:00', 'Visita de rotina, sem focos aparentes.', 'CONCLUIDA', 28.5, TRUE),
       (2, 3, '2025-06-17 11:30:00', 'Encontrados pequenos focos em pneus velhos.', 'CONCLUIDA', 29.1, TRUE),
       (1, 2, '2025-06-17 15:00:00', 'Morador ausente, reagendado.', 'PENDENTE', NULL, FALSE),
       (3, 5, '2025-06-16 09:00:00', 'Inspeção predial, sem irregularidades.', 'CONCLUIDA', 27.8, TRUE),
       (4, 7, '2025-06-17 09:30:00', 'Área de risco, muitos recipientes com água parada.', 'CONCLUIDA', 29.5, TRUE);

-- Inserções para a tabela FocoAedes (usando os IDs de Visita que você informou: 10, 11, 12, 13, 14)
INSERT INTO FocoAedes (visita_id, imagem, tipoFoco, quantidade, tratado, observacoes)
VALUES (2, 'foco_pneu_velho_1.jpg', 'LARVA', 30, TRUE, 'Pneus tratados com larvicida.'),
       (3, 'foco_vaso_flor.jpg', 'OVO', 50, TRUE, 'Vaso de flor vazio e virado.'),
       (1, 'foco_caixa_dagua.jpg', 'LARVA', 100, FALSE, 'Caixa dágua destampada, notificado morador.'),
       (5, 'foco_ralo.jpg', 'ADULTO', 5, TRUE, 'Ralo com acúmulo de água, aplicação de inseticida.');
(13, 5, '2025-06-12', 'DENGUE_COM_SINAIS_DE_ALARME', 'MODERADA', 'Febre alta, dor abdominal intensa, vômitos persistentes.', 'Encaminhado para UPA.', TRUE);

INSERT INTO CasoDengue (visita_id, paciente_id, dataDiagnostico, tipoDengue, gravidade, sintomas, observacoes,
                        confirmadoLab)
VALUES (1, 2, '2025-06-10', 'DENGUE_CLASSIFICADA', 'LEVE', 'Febre, dor de cabeça, dor no corpo.',
        'Paciente já em recuperação.', TRUE),
       (4, 5, '2025-06-12', 'DENGUE_COM_SINAIS_DE_ALARME', 'MODERADA',
        'Febre alta, dor abdominal intensa, vômitos persistentes.', 'Encaminhado para UPA.', TRUE);