CREATE TABLE public.jovial_parties
(
    id BIGSERIAL PRIMARY KEY,
    jovial_id BIGINT REFERENCES jovial (jovial_id),
    party_id BIGINT REFERENCES virtual_parties (party_id),
    created_time TIMESTAMP NOT NULL,
    created_jovial VARCHAR (256) NOT NULL
);