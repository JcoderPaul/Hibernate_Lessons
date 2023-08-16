CREATE TABLE IF NOT EXISTS public.deans
(
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    birth_data DATE NOT NULL,
    faculty VARCHAR(256),
    salary DOUBLE PRECISION,
    PRIMARY KEY (firstname, lastname, birth_data)
);