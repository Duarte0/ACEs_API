CREATE TABLE Area (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      nome VARCHAR(100) NOT NULL,
                      descricao TEXT,
                      regiao VARCHAR(50),
                      populacaoAprox INT,
                      nivelRisco VARCHAR(20),
                      prioridade VARCHAR(20),
                      dataUltimaAtt DATETIME
);


CREATE TABLE Endereco (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          area_id INT NOT NULL,
                          logradouro VARCHAR(100) NOT NULL,
                          numero VARCHAR(20),
                          complemento VARCHAR(100),
                          bairro VARCHAR(50) NOT NULL,
                          cep VARCHAR(10) NOT NULL,
                          latitude DECIMAL(10, 8),
                          longitude DECIMAL(11, 8),
                          spolmovel BOOLEAN,
                          FOREIGN KEY (area_id) REFERENCES Area(id)
);


CREATE TABLE Paciente (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          endereco_id INT NOT NULL,
                          nome VARCHAR(100) NOT NULL,
                          dataNascimento DATE,
                          sexo CHAR(1),
                          cpf VARCHAR(14) UNIQUE,
                          cartaoSUS VARCHAR(20),
                          telefone VARCHAR(20),
                          FOREIGN KEY (endereco_id) REFERENCES Endereco(id)
);


CREATE TABLE Usuario (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         nome VARCHAR(100) NOT NULL,
                         cpf VARCHAR(14) UNIQUE NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         telefone VARCHAR(20),
                         cargo VARCHAR(50),
                         ativo BOOLEAN DEFAULT TRUE,
                         ultimoAcesso DATETIME,
                         dataCriacao DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Agente (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        usuario_id INT NOT NULL,
                        matricula VARCHAR(20) UNIQUE NOT NULL,
                        dataAdmissao DATE,
                        dataInicio DATE,
                        dataFim DATE,
                        ativo BOOLEAN DEFAULT TRUE,
                        FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
                        CONSTRAINT chk_datas CHECK (dataFim IS NULL OR dataFim >= dataInicio)
);


CREATE TABLE AgenteArea (
                            agente_id INT,
                            area_id INT,
                            PRIMARY KEY (agente_id, area_id),
                            FOREIGN KEY (agente_id) REFERENCES Agente(id),
                            FOREIGN KEY (area_id) REFERENCES Area(id)
);


CREATE TABLE Visita (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        agente_id INT NOT NULL,
                        endereco_id INT NOT NULL,
                        dataHora DATETIME NOT NULL,
                        observacoes TEXT,
                        status VARCHAR(20) NOT NULL,
                        temperatura DECIMAL(4, 2),
                        foiRealizada BOOLEAN DEFAULT FALSE,
                        FOREIGN KEY (agente_id) REFERENCES Agente(id),
                        FOREIGN KEY (endereco_id) REFERENCES Endereco(id)
);


CREATE TABLE CasoDengue (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            visita_id INT NOT NULL,
                            paciente_id INT NOT NULL,
                            dataDiagnostico DATE NOT NULL,
                            tipoDengue VARCHAR(50),
                            gravidade VARCHAR(20),
                            sintomas TEXT,
                            observacoes TEXT,
                            confirmadoLab BOOLEAN DEFAULT FALSE,
                            FOREIGN KEY (visita_id) REFERENCES Visita(id),
                            FOREIGN KEY (paciente_id) REFERENCES Paciente(id)
);


CREATE TABLE FocoAedes (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           visita_id INT NOT NULL,
                           imagem VARCHAR(255),
                           tipoFoco VARCHAR(50),
                           quantidade INT,
                           tratado BOOLEAN DEFAULT FALSE,
                           observacoes TEXT,
                           FOREIGN KEY (visita_id) REFERENCES Visita(id)
);


CREATE INDEX idx_endereco_area ON Endereco(area_id);
CREATE INDEX idx_paciente_endereco ON Paciente(endereco_id);
CREATE INDEX idx_agente_usuario ON Agente(usuario_id);
CREATE INDEX idx_visita_agente ON Visita(agente_id);
CREATE INDEX idx_visita_endereco ON Visita(endereco_id);
CREATE INDEX idx_caso_visita ON CasoDengue(visita_id);
CREATE INDEX idx_caso_paciente ON CasoDengue(paciente_id);
CREATE INDEX idx_foco_visita ON FocoAedes(visita_id);