--liquibase formatted sql

CREATE TABLE participant_roles (
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);