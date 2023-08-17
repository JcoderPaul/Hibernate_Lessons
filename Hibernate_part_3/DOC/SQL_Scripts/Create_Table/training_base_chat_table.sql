CREATE TABLE training_base.chats
(
    chat_id BIGSERIAL PRIMARY KEY,
    chat_name VARCHAR(64) NOT NULL UNIQUE
);