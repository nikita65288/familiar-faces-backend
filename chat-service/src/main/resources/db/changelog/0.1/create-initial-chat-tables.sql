--liquibase formatted sql

CREATE SEQUENCE chats_id_seq;
CREATE SEQUENCE messages_id_seq;

CREATE TABLE chats (
    id BIGINT DEFAULT nextval('chats_id_seq') NOT NULL,
    type_id INT NOT NULL,
    name VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT chats_pk PRIMARY KEY (id),
    CONSTRAINT type_id_fk FOREIGN KEY (type_id) REFERENCES chat_types(id)
);

CREATE TABLE chat_participants (
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    joined_at TIMESTAMP NOT NULL,
    PRIMARY KEY (chat_id, user_id),
    CONSTRAINT chat_id_fk FOREIGN KEY (chat_id) REFERENCES chats(id),
    CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES participant_roles(id)
);

CREATE TABLE messages (
    id BIGINT DEFAULT nextval('messages_id_seq') NOT NULL,
    chat_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_read BOOLEAN NOT NULL,
    CONSTRAINT chat_id_fk FOREIGN KEY (chat_id) REFERENCES chats(id)
);