CREATE TABLE IF NOT EXISTS part_four_base.meeting_rooms
(
    room_id BIGSERIAL PRIMARY KEY,
    room_name VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMP,
    created_by VARCHAR(256),
    updated_at TIMESTAMP,
    updated_by VARCHAR(256),
    enter_price INT
);

/*
Внесем изменения в таблицу users_chats (добавим еще два поля).
Есть вариант внести изменения мягко с сохранением данных, но
мы поступим как истинные варвары:
*/
DROP TABLE part_four_base.users_chats;

CREATE TABLE IF NOT EXISTS part_four_base.users_chats
(
    users_chats_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES part_four_base.users (user_id),
    chat_id BIGINT REFERENCES part_four_base.chats (chat_id),
    created_at TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    UNIQUE (user_id, chat_id)
);

/* Внесем изменения в таблицу chat */
ALTER TABLE part_four_base.chats
ADD count INTEGER NULL;

/* Создадим таблицу audit */
CREATE TABLE IF NOT EXISTS part_four_base.audit
(
    id BIGSERIAL PRIMARY KEY,
    entity_id bytea,
    entity_name VARCHAR(256),
    entity_content VARCHAR(256),
    operation VARCHAR(32)
);
