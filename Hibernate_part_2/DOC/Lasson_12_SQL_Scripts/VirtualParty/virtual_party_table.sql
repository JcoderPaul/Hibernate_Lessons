CREATE TABLE public.virtual_parties
(
    party_id BIGSERIAL PRIMARY KEY,
    party_name VARCHAR(128) NOT NULL UNIQUE
);