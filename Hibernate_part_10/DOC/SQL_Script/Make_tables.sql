/*
На этот раз мы создали новую базу и все манипуляции
будем делать внутри schema = 'public'
*/

CREATE TABLE IF NOT EXISTS company
(
    company_id SERIAL PRIMARY KEY ,
    company_name VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS company_locale
(
    company_id INT NOT NULL REFERENCES company (company_id),
    lang CHAR(3) NOT NULL,
    description VARCHAR(128) NOT NULL,
    PRIMARY KEY (company_id, lang)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    user_name VARCHAR(128) UNIQUE,
    role VARCHAR(32),
    info JSONB,
    company_id INT REFERENCES company (company_id)
);

CREATE TABLE IF NOT EXISTS profile
(
    profile_id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(user_id),
    street VARCHAR(128),
    language CHAR(3)
);

CREATE TABLE IF NOT EXISTS chats
(
    chat_id BIGSERIAL PRIMARY KEY,
    chat_name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_chats
(
    users_chats_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (user_id),
    chat_id BIGINT REFERENCES chats (chat_id),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(128) NOT NULL,
    UNIQUE (user_id, chat_id)
);

CREATE TABLE IF NOT EXISTS payments
(
    id BIGSERIAL PRIMARY KEY ,
    amount INTEGER NOT NULL ,
    receiver_id BIGINT NOT NULL UNIQUE REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS revision_records
(
    rev_id BIGSERIAL PRIMARY KEY ,
    rev_time_stamp BIGINT ,
    user_name VARCHAR(256)
);