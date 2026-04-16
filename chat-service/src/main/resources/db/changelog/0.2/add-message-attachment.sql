--liquibase formatted sql
ALTER TABLE messages ADD COLUMN attachment_url VARCHAR(512);
ALTER TABLE chats ALTER COLUMN avatar_url TYPE VARCHAR(512);

