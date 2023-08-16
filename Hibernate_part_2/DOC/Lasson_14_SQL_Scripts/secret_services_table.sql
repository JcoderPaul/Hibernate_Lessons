CREATE TABLE IF NOT EXISTS public.secret_services
(
    bureau_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    bureau_name VARCHAR(128) NOT NULL UNIQUE,
    CONSTRAINT bureau_pkey PRIMARY KEY (bureau_id)
);

INSERT INTO public.secret_services(
    bureau_name)
VALUES ('KGB'),
       ('MOSSAD'),
       ('MI6'),
       ('STASI');