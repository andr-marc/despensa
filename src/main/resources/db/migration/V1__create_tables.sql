BEGIN;

CREATE SCHEMA IF NOT EXISTS entidades;
CREATE TABLE IF NOT EXISTS entidades.produto
(
    id uuid NOT NULL,
    titulo character varying NOT NULL,
    codigo_barra character varying NOT NULL,
    marca character varying NOT NULL,
    data_validade character varying NOT NULL,
    data_compra character varying NOT NULL,
    status character varying NOT NULL,
    categoria uuid NOT NULL,
    quantidade uuid NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS entidades.categoria
(
    id uuid NOT NULL,
    titulo character varying,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS entidades.quantidade
(
    id uuid NOT NULL,
    unidade integer NOT NULL,
    subunidade integer,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS entidades.movimentacao
(
    id uuid NOT NULL,
    produto uuid NOT NULL,
    consumo character varying NOT NULL,
    autor uuid NOT NULL,
    created_at character varying NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS entidades.usuario
(
    id uuid NOT NULL,
    nome character varying NOT NULL,
    login character varying NOT NULL,
    senha character varying NOT NULL,
    PRIMARY KEY (id)
    );

ALTER TABLE IF EXISTS entidades.produto
    ADD FOREIGN KEY (categoria)
    REFERENCES entidades.categoria (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS entidades.produto
    ADD FOREIGN KEY (quantidade)
    REFERENCES entidades.quantidade (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS entidades.movimentacao
    ADD FOREIGN KEY (produto)
    REFERENCES entidades.produto (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS entidades.movimentacao
    ADD FOREIGN KEY (autor)
    REFERENCES entidades.usuario (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;

END;