CREATE TABLE IF NOT EXISTS "usuario" (
    "id_usuario" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "nm_email" VARCHAR(254) NOT NULL UNIQUE,
    "nm_usuario" VARCHAR(255),
    "nm_senha" VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS "categoria" (
    "id_categoria" INT PRIMARY KEY,
    "nm_categoria" VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS "lancamento" (
    "id_lancamento" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "id_categoria" BIGINT,
    "dt_lancamento" DATE,
    "nm_lancamento" VARCHAR(255),
    "cd_tipo" CHAR(7) CHECK ("cd_tipo" IN ('Despesa', 'Receita')),
    "vl_lancamento" NUMERIC(38,2),
    "id_usuario" BIGINT NOT NULL
);
CREATE TABLE IF NOT EXISTS "saldo" (
    "id_saldo" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "vl_saldo_atual" NUMERIC(38,2),
    "id_usuario" BIGINT NOT NULL
);
ALTER TABLE "lancamento"
    ADD CONSTRAINT "fk_lancamento_categoria" FOREIGN KEY ("id_categoria") REFERENCES "categoria" ("id_categoria");
ALTER TABLE "lancamento"
    ADD CONSTRAINT "fk_lancamento_usuario" FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id_usuario");
ALTER TABLE "saldo"
    ADD CONSTRAINT "fk_saldo_usuario" FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id_usuario");
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (1, 'Alimentação');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (2, 'Transporte');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (3, 'Moradia');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (4, 'Saúde');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (5, 'Educação');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (6, 'Lazer');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (7, 'Roupas');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (8, 'Outros');
MERGE INTO "categoria" ("id_categoria", "nm_categoria") KEY("id_categoria") VALUES (9, 'Trabalho');
