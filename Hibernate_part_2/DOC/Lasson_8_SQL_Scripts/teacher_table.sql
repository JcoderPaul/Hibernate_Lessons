CREATE TABLE IF NOT EXISTS public.teachers
(
    id bigint NOT NULL,
    teacher_name varchar(256) unique,
    teacher_email varchar(128),
    age integer,
    PRIMARY KEY (id)
);

create table all_sequence
(
    table_name VARCHAR(32) PRIMARY KEY,
    pk_value BIGINT NOT NULL
);