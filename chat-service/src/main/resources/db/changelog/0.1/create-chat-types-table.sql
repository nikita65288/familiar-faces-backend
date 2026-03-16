--liquibase formatted sql

CREATE TABLE chat_types (
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);
