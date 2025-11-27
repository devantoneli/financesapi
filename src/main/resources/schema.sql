-- Criar tabelas se não existirem
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nome VARCHAR(255),
    senha VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS transacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(255),
    data DATE,
    descricao VARCHAR(255),
    tipo VARCHAR(255),
    valor NUMERIC(38,2),
    usuario_id BIGINT NOT NULL,
    CONSTRAINT FKnnwmcpelyv6nuwrwixo6ovuv1 FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);
