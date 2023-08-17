CREATE TABLE IF NOT EXISTS part_four_base.employee
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    employee_email VARCHAR(128) UNIQUE
);

INSERT INTO part_four_base.employee
(first_name, last_name, birth_date, employee_email)
VALUES ('Aleksander', 'Kochev', '1989-02-18', 'alek_admin@enerprise.com'),
       ('Vitaliy', 'Zacutin', '2001-02-22', 'vitek_utek@enerprise.com'),
       ('Aleksander', 'Valdau-Coster', '1987-03-18', 'base_master@enerprise.com'),
       ('Sergey', 'Zaburin', '2003-07-07', 'prodavan_one@enerprise.com'),
       ('Sergey', 'Nikitin', '1998-07-21', 'grey_boss@enerprise.com'),
       ('Aleksander', 'Fifsoff', '1996-04-15', 'imperrorr@enerprise.com'),
       ('Anna', 'Dolgaya', '2008-03-15', 'dorter@enerprise.com'),
       ('Tamila', 'Zakiny', '2001-09-19', 'queen_of_pain@enerprise.com'),
       ('Vitaliy', 'Eremeev', '1889-05-14', 'zakat@enerprise.com');