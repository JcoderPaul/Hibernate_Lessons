CREATE TABLE training_base.users_chats
(
    users_chats_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES training_base.users (user_id),
    chat_id BIGINT REFERENCES training_base.chats (chat_id),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(128) NOT NULL,
    UNIQUE (user_id, chat_id)
);