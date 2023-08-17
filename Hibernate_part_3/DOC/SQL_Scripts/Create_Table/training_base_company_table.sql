CREATE TABLE training_base.company
(
    company_id SERIAL PRIMARY KEY ,
    company_name VARCHAR(128) NOT NULL UNIQUE
);