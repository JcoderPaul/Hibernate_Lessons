CREATE SCHEMA IF NOT EXISTS part_four_base;

CREATE TABLE IF NOT EXISTS part_four_base.users
(
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    user_name VARCHAR(128) UNIQUE,
    role VARCHAR(32),
    info JSONB,
    company_id INT REFERENCES part_four_base.company (company_id)
);

CREATE TABLE IF NOT EXISTS part_four_base.users_chats
(
    users_chats_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES part_four_base.users (user_id),
    chat_id BIGINT REFERENCES part_four_base.chats (chat_id),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(128) NOT NULL,
    UNIQUE (user_id, chat_id)
);

CREATE TABLE IF NOT EXISTS part_four_base.company_locale
(
    company_id INT NOT NULL REFERENCES part_four_base.company (company_id),
    lang CHAR(3) NOT NULL,
    description VARCHAR(128) NOT NULL,
    PRIMARY KEY (company_id, lang)
);

CREATE TABLE IF NOT EXISTS part_four_base.chats
(
    chat_id BIGSERIAL PRIMARY KEY,
    chat_name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS part_four_base.profile
(
    profile_id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL UNIQUE REFERENCES part_four_base.users(user_id),
    street VARCHAR(128),
    language CHAR(3)
);

CREATE TABLE IF NOT EXISTS part_four_base.company
(
    company_id SERIAL PRIMARY KEY ,
    company_name VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS part_four_base.payment
(
    id SERIAL PRIMARY KEY ,
    amount INTEGER NOT NULL ,
    receiver_id BIGINT NOT NULL REFERENCES part_four_base.users(user_id)
);

/* Таблицы для изучения уровней изолированности транзакций */
CREATE TABLE IF NOT EXISTS part_four_base.workers
(
    worker_id BIGSERIAL PRIMARY KEY,
    version BIGSERIAL,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    amount INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS part_four_base.students
(
    stud_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    scholarship INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS part_four_base.schoolboys
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    scholarship INTEGER NOT NULL
);