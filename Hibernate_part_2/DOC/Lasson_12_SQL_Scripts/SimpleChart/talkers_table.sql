CREATE TABLE public.talkers
(
    talker_id BIGSERIAL PRIMARY KEY,
    talker_name VARCHAR(128) NOT NULL UNIQUE,
    talker_age INTEGER
);