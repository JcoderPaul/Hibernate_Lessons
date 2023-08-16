CREATE TABLE IF NOT EXISTS public.drivers
(
    driver_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    driver_name VARCHAR(256) NOT NULL,
    experience INTEGER,
    salary DOUBLE PRECISION,
    CONSTRAINT driver_pkey PRIMARY KEY (driver_id)
);