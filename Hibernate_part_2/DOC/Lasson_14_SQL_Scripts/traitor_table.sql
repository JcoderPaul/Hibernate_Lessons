CREATE TABLE public.traitors
(
    traitor_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    traitor_name VARCHAR(256),
    salary DOUBLE PRECISION,
    bureau_id INT REFERENCES public.secret_services (bureau_id),
    CONSTRAINT traitor_pkey PRIMARY KEY (traitor_id)
);

INSERT INTO public.traitors(
    traitor_name, salary, bureau_id)
VALUES ('Sergey Dorohov', 12300,1),
       ('Anton Polyakov',5400,1),
       ('Keem Fealby',8700,3),
       ('Klaus Fooks',5600,3),
       ('Jordee Blayk',7500,3),
       ('Alldredj Aymse',9900,3);