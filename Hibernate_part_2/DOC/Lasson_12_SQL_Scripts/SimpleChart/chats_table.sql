CREATE TABLE public.chats
(
    chat_id BIGSERIAL PRIMARY KEY,
    chat_name VARCHAR(128) NOT NULL UNIQUE
);