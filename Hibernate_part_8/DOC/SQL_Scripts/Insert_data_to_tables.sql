/* Добавляем записи в БД по компаниям */
INSERT INTO part_four_base.company
(company_name)
VALUES ('Google'),
       ('Yandex'),
       ('Facebook'),
       ('Polus'),
       ('Sony'),
       ('Microsoft'),
       ('Apple');

/* Добавляем записи в БД по чатам */
INSERT INTO part_four_base.chats
(chat_name)
VALUES ('Historical chat'),
       ('Physical chat'),
       ('Secret chat'),
       ('Hogwarts chat'),
       ('German chat'),
       ('Java chat'),
       ('YouTube chat');

/* Добавляем записи в БД по 'локалям - языкам' */
INSERT INTO part_four_base.company_locale
(company_id, lang, description)
VALUES (1, 'ENG', 'English language'),
       (2, 'RUS', 'Russian language'),
       (3, 'ENG', 'English language'),
       (4, 'RUS', 'Russian language'),
       (5, 'JAP', 'Japanese language');

/* Добавляем записи в БД по пользователям */
INSERT INTO part_four_base.users
(first_name, last_name, birth_date, user_name, role, info, company_id)
VALUES ('Tasha', 'Yar', '2645-02-18', 'fighter@enerprise.com', 'ADMIN', '{}', 1),
       ('Jordy', 'LaForge', '2665-02-22', 'vizor@enerprise.com', 'ADMIN', '{}', 1),
       ('Garibo', 'Coply', '2965-03-18', 'garicop@google.com', 'USER', '{}', 1),
       ('Aerdol', 'T-Qute', '1325-07-07', 'aracy@yandex.ru', 'ADMIN', '{}', 2),
       ('Star', 'Lord', '1965-07-21', 'starboy@yahoo.com', 'USER', '{}', 1),
       ('Willy', 'Wonka', '1901-04-15', 'ww@chocolate.com', 'ADMIN', '{}', 3),
       ('Willy', 'Bushew', '1908-03-15', 'willi@persident.com', 'ADMIN', '{}', 4),
       ('Willy', 'Ambush', '1918-09-19', 'shpily_willi@wet.com', 'ADMIN', '{}', 3),
       ('Bary', 'Gudkayn', '1465-05-14', 'btk@unyc.com', 'USER', '{}', 1),
       ('Willy', 'Bushew', '1908-03-15', 'willi@expersident.com', 'ADMIN', '{}', 1),
       ('Bill', 'Gates', '1955-10-28', 'bil@microsoft.com', 'ADMIN', '{}', 6),
       ('Steve', 'Jobs', '1955-02-24', 'steve@apple.com', 'ADMIN', '{}', 7),
       ('Sergey', 'Brin', '1973-08-21', 'sb@google.com', 'ADMIN', '{}', 1),
       ('Tim', 'Cook', '1960-11-1', 'timmiboy@google.com', 'ADMIN', '{}', 7),
       ('Diane', 'Greene', '1955-01-1', 'diane_bg@google.com', 'ADMIN', '{}', 1);


/* Добавляем записи в БД по профилям */
INSERT INTO part_four_base.profile
(user_id, street, language)
VALUES (1, 'Nejin st. 23', 'RUS'),
       (2, 'Kapitol av. 1234 aprt.42', 'ENG'),
       (3, 'Baus 23', 'FRA'),
       (4, 'TuMany Kort 543', 'ELF'),
       (5, 'Batista st. 1225', 'ENG'),
       (6, 'Cambly Stor st. 532', 'ENG'),
       (7, '1-st st. 231 aprt 34.', 'ENG'),
       (8, 'Cambly Stor st. 5432', 'ENG'),
       (9, '2-st st. 2231 aprt 324.', 'ENG'),
       (10, '1-st st. 231 aprt 34.', 'ENG'),
       (11, '332-st st. 11 aprt 3.', 'ENG'),
       (12, 'Kapuchino av 11 aprt 2234.', 'ENG'),
       (13, 'Takun st. 2231 aprt 4533.', 'ENG'),
       (14, 'Kladinoty st. 261 aprt 8834.', 'ENG'),
       (15, 'Pritory prk. av. 12. ', 'ENG');

/* Добавляем записи в БД по связной таблице user-chat */
INSERT INTO part_four_base.users_chats
(user_id, chat_id, created_at, created_by)
VALUES (2, 3, '2023-07-18 11:25:54.931277', 'Uryi'),
       (3, 4, '2023-07-16 12:35:54.931275', 'Katlyn'),
       (11, 6, '2012-03-18 11:25:54.931277', 'Billi'),
       (12, 6, '2012-03-18 11:25:54.931277', 'Billi'),
       (13, 6, '2012-03-18 11:25:54.931277', 'Billi'),
       (11, 7, '2001-01-11 11:25:54.931275', 'Steavee'),
       (12, 7, '2001-01-11 11:25:54.931275', 'Steavee'),
       (14, 7, '2001-01-11 11:25:54.931275', 'Steavee'),
       (15, 7, '2001-01-11 11:25:54.931275', 'Steavee'),
       (11, 3, '2003-09-12 14:45:54.931277', 'Timmi'),
       (12, 3, '2003-09-12 14:45:54.931277', 'Timmi'),
       (14, 3, '2003-09-12 14:45:54.931277', 'Timmi'),
       (15, 3, '2003-09-12 14:45:54.931277', 'Timmi');

/* Добавляем записи в БД в таблицу payment */
INSERT INTO part_four_base.payment
    (amount, receiver_id)
VALUES (500, 1),
       (600, 2),
       (400, 3),
       (600, 4),
       (300, 5),
       (500, 6),
       (700, 7),
       (300, 8),
       (500, 10),
       (300, 9),
       (100, 11),
       (300, 11),
       (500, 11),
       (250, 12),
       (600, 12),
       (500, 12),
       (400, 14),
       (300, 14),
       (500, 13),
       (500, 13),
       (500, 13),
       (300, 15),
       (300, 15),
       (300, 15);

/* Таблицы для изучения уровней изолированности транзакций */
INSERT INTO part_four_base.workers
(first_name, last_name, amount, version)
VALUES ('Tasha', 'Yar', 2354, 0),
       ('Jordy', 'LaForge', 4352, 0),
       ('Garibo', 'Coply', 1213, 0),
       ('Aerdol', 'T-Qute', 4231, 0),
       ('Star', 'Lord', 2314, 0);

INSERT INTO part_four_base.students
(first_name, last_name, scholarship)
VALUES ('Thor', 'Odinson', 2354),
       ('Kerio', 'Takahiro', 435),
       ('Patang', 'Dgablakur', 1213),
       ('Sarachi', 'Ruki', 4231),
       ('Tamara', 'Rahmonova', 2314);

INSERT INTO part_four_base.schoolboys
(first_name, last_name, scholarship)
VALUES ('Masha', 'Pushina', 354),
       ('Vasya', 'Petrov', 435),
       ('Petya', 'Vasechkin', 213),
       ('Vintik', 'Shpunticov', 231),
       ('Znayka', 'Vishenosov', 314);