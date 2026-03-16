--liquibase formatted sql

-- init chat types
INSERT INTO chat_types (id, name) VALUES (1, 'PRIVATE'), (2, 'GROUP');

-- init participant roles
INSERT INTO participant_roles (id, name) VALUES (1, 'MEMBER'), (2, 'ADMIN');