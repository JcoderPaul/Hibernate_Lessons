CREATE TABLE public.doubles
(
    double_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    double_name VARCHAR(256),
    salary DOUBLE PRECISION,
    bureau_id INT REFERENCES public.secret_services (bureau_id),
    CONSTRAINT doubles_pkey PRIMARY KEY (double_id)
);

INSERT INTO public.doubles(
    double_name, salary, bureau_id)
VALUES ('Dushan Popov', 12300,6),
       ('Oleg Penkovsky',5400,1),
       ('Humam Al Balavy',8700,6),
       ('Artur Ouens',5600,3),
       ('Oldrich Ayens',7500,6),
       ('Keym Fealby',9900,3),
       ('Huan Garsya Pushol',7500,3);