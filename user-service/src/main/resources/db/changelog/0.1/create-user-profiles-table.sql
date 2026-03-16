--liquibase formatted sql

CREATE SEQUENCE user_profiles_id_seq;

CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT DEFAULT nextval('user_profiles_id_seq') NOT NULL,
    auth_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    bio VARCHAR(255),
    avatar_url VARCHAR(255),
    CONSTRAINT user_profiles_pk PRIMARY KEY (id)
);