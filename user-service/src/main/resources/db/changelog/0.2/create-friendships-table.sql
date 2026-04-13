--liquibase formatted sql

CREATE SEQUENCE friendships_id_seq;

CREATE TABLE IF NOT EXISTS friendships (
    id BIGINT DEFAULT nextval('friendships_id_seq') NOT NULL,
    requester_id BIGINT NOT NULL,
    addressee_id BIGINT NOT NULL,
    status_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT friendships_pk PRIMARY KEY (id),
    CONSTRAINT friendships_requester_addressee_uk UNIQUE (requester_id, addressee_id),
    CONSTRAINT friendships_no_self_ck CHECK (requester_id <> addressee_id)
);