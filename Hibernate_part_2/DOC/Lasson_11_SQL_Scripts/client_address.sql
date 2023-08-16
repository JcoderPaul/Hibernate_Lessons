CREATE TABLE public.addresses
(
    address_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    driver_id INT NOT NULL UNIQUE REFERENCES public.drivers (driver_id),
    street VARCHAR(128),
    language CHAR(2),
    CONSTRAINT address_pkey PRIMARY KEY (address_id)
);