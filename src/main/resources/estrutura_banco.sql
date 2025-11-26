CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100),
    senha VARCHAR(255)
);

CREATE TABLE caminhao (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(20),
    motorista VARCHAR(100),
    capacidade DOUBLE PRECISION
);

CREATE TABLE residuo (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(100)
);

CREATE TABLE bairro (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100)
);

CREATE TABLE rua (
    id BIGSERIAL PRIMARY KEY,
    origem_id BIGINT,
    destino_id BIGINT,
    distancia_km DOUBLE PRECISION,
    FOREIGN KEY (origem_id) REFERENCES bairro(id),
    FOREIGN KEY (destino_id) REFERENCES bairro(id)
);

CREATE TABLE rota (
    id BIGSERIAL PRIMARY KEY,
    caminhao_id BIGINT,
    distancia_total DOUBLE PRECISION,
    FOREIGN KEY (caminhao_id) REFERENCES caminhao(id)
);

CREATE TABLE rota_bairros (
    rota_id BIGINT,
    bairro_id BIGINT,
    PRIMARY KEY (rota_id, bairro_id),
    FOREIGN KEY (rota_id) REFERENCES rota(id),
    FOREIGN KEY (bairro_id) REFERENCES bairro(id)
);

CREATE TABLE rota_ruas (
    rota_id BIGINT,
    rua_id BIGINT,
    PRIMARY KEY (rota_id, rua_id),
    FOREIGN KEY (rota_id) REFERENCES rota(id),
    FOREIGN KEY (rua_id) REFERENCES rua(id)
);

CREATE TABLE rota_tipos_residuos (
    rota_id BIGINT,
    tipo_residuo_id BIGINT,
    PRIMARY KEY (rota_id, tipo_residuo_id),
    FOREIGN KEY (rota_id) REFERENCES rota(id),
    FOREIGN KEY (tipo_residuo_id) REFERENCES residuo(id)
);

CREATE TABLE caminhao_tipos_residuos (
    caminhao_id BIGINT,
    tipos_residuos_id BIGINT,
    PRIMARY KEY (caminhao_id, tipos_residuos_id),
    FOREIGN KEY (caminhao_id) REFERENCES caminhao(id),
    FOREIGN KEY (tipos_residuos_id) REFERENCES residuo(id)
);

CREATE TABLE ponto_coleta (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100),
    responsavel VARCHAR(100),
    telefone_responsavel VARCHAR(20),
    email_responsavel VARCHAR(100),
    endereco VARCHAR(255),
    horario VARCHAR(100),
    bairro_id BIGINT,
    FOREIGN KEY (bairro_id) REFERENCES bairro(id)
);

CREATE TABLE ponto_coleta_tipos_residuos (
    ponto_coleta_id BIGINT,
    tipos_residuos_id BIGINT,
    PRIMARY KEY (ponto_coleta_id, tipos_residuos_id),
    FOREIGN KEY (ponto_coleta_id) REFERENCES ponto_coleta(id),
    FOREIGN KEY (tipos_residuos_id) REFERENCES residuo(id)
);

CREATE TABLE itinerario (
    id BIGSERIAL PRIMARY KEY,
    data DATE,
    rota_id BIGINT,
    FOREIGN KEY (rota_id) REFERENCES rota(id)
);

CREATE TABLE log_usuario (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT,
    operacao VARCHAR(10),
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    detalhes TEXT
);

CREATE TABLE log_ponto_coleta (
    id SERIAL PRIMARY KEY,
    ponto_coleta_id BIGINT,
    operacao VARCHAR(10),
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    detalhes TEXT
);

-- Triggers para controle de log
CREATE OR REPLACE FUNCTION log_usuario_operacoes() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO log_usuario (usuario_id, operacao, detalhes)
        VALUES (NEW.id, 'INSERT', CONCAT('Novo usuário: ', NEW.nome));
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO log_usuario (usuario_id, operacao, detalhes)
        VALUES (NEW.id, 'UPDATE', CONCAT('Atualização de dados para: ', NEW.nome));
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO log_usuario (usuario_id, operacao, detalhes)
        VALUES (OLD.id, 'DELETE', CONCAT('Usuário removido: ', OLD.nome));
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_usuario
AFTER INSERT OR UPDATE OR DELETE ON usuario
FOR EACH ROW EXECUTE FUNCTION log_usuario_operacoes();

CREATE OR REPLACE FUNCTION log_ponto_coleta_operacoes() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO log_ponto_coleta (ponto_coleta_id, operacao, detalhes)
        VALUES (NEW.id, 'INSERT', CONCAT('Novo ponto: ', NEW.nome));
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO log_ponto_coleta (ponto_coleta_id, operacao, detalhes)
        VALUES (NEW.id, 'UPDATE', CONCAT('Atualização de ponto: ', NEW.nome));
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO log_ponto_coleta (ponto_coleta_id, operacao, detalhes)
        VALUES (OLD.id, 'DELETE', CONCAT('Ponto removido: ', OLD.nome));
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_ponto_coleta
AFTER INSERT OR UPDATE OR DELETE ON ponto_coleta
FOR EACH ROW EXECUTE FUNCTION log_ponto_coleta_operacoes();
