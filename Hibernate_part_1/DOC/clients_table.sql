CREATE TABLE IF NOT EXISTS public.clients
(
    client_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    client_name character varying(128) COLLATE pg_catalog."default",
    birth_data date,
    info jsonb,
    CONSTRAINT client_pkey PRIMARY KEY (client_id)
)