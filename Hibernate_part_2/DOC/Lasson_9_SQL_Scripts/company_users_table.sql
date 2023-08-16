CREATE TABLE public.company_users
(
    user_id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(128),
    lastname VARCHAR(128),
    birth_data DATE,
    username VARCHAR(32),
    role VARCHAR(32),
    company_id INT REFERENCES public.companies (company_id)
);