CREATE TABLE training_base.profile
(
    profile_id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL UNIQUE REFERENCES training_base.users(user_id),
    street VARCHAR(128),
    language CHAR(2)
);