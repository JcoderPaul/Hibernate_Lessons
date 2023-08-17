CREATE TABLE training_base.users
(
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    user_name VARCHAR(128) UNIQUE,
    role VARCHAR(32),
    info JSONB,
    company_id INT REFERENCES training_base.company (company_id)
);