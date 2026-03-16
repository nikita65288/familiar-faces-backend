--liquibase formatted sql

ALTER TABLE messages ADD CONSTRAINT messages_pk PRIMARY KEY (id);

ALTER TABLE messages
    ALTER COLUMN id SET DEFAULT nextval('messages_id_seq');

ALTER SEQUENCE messages_id_seq OWNED BY messages.id;

SELECT setval('messages_id_seq', COALESCE(MAX(id), 0) + 1, false) FROM messages;