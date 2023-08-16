CREATE TABLE public.spies
(
    spy_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    spy_name VARCHAR(256),
    salary DOUBLE PRECISION,
    bureau_id INT REFERENCES public.secret_services (bureau_id),
    CONSTRAINT spy_pkey PRIMARY KEY (spy_id)
);

INSERT INTO public.spies(
    spy_name, salary, bureau_id)
VALUES ('Jeyms Bond', 12500, 3),
       ('Eteli Rosenberg', 8950, 1),
       ('Gunter Giyom', 11340, 2),
       ('Yolanta Harmer', 9300, 4),
       ('Dafna Park', 7500, 3),
       ('Brus Lokkart', 5300, 3),
       ('Sidney Reyli', 18300, 3),
       ('Somerset Moem', 19100, 3);
