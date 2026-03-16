--liquibase formatted sql

CREATE SEQUENCE user_credentials_id_seq;

CREATE TABLE IF NOT EXISTS user_credentials (
    id BIGINT DEFAULT nextval('user_credentials_id_seq') NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT user_credentials_pk PRIMARY KEY (id)
);
