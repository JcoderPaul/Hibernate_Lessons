CREATE TABLE public.firm_workers
(
    worker_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    worker_name VARCHAR(256),
    salary DOUBLE PRECISION,
    firm_id INT REFERENCES public.firms (firm_id),
    CONSTRAINT worker_pkey PRIMARY KEY (worker_id)
);