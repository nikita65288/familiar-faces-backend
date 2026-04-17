--liquibase formatted sql
CREATE SEQUENCE message_reactions_id_seq;

CREATE TABLE IF NOT EXISTS message_reactions (
    id BIGINT DEFAULT nextval('message_reactions_id_seq') NOT NULL,
    message_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    emoji VARCHAR(10) NOT NULL,
    UNIQUE (message_id, user_id, emoji)
);
