CREATE TABLE IF NOT EXISTS students
(
    stud_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    scholarship INTEGER NOT NULL
);
