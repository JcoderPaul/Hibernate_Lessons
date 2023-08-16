CREATE TABLE IF NOT EXISTS public.companies
(
    company_id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);