CREATE TABLE public.users
(
    username VARCHAR(128) PRIMARY KEY,
    firstname VARCHAR(128),
    lastname VARCHAR(128),
    birth_data DATE,
    role VARCHAR(32)
);