CREATE TABLE public.profile
(
    profile_id INT PRIMARY KEY REFERENCES clients.users (client_id),
    street VARCHAR(128),
    language CHAR(2)
);