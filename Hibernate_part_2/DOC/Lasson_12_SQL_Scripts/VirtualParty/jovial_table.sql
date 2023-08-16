CREATE TABLE public.jovial
(
    jovial_id BIGSERIAL PRIMARY KEY,
    jovial_name VARCHAR(128) NOT NULL UNIQUE,
    jovial_age INTEGER
);