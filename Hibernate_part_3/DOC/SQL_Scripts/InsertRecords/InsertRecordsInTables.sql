/* Добавляем записи в БД по компаниям */
INSERT INTO training_base.company
    (company_name)
VALUES ('Google'),
       ('Yandex'),
       ('Facebook'),
       ('Polus'),
       ('Sony');

/* Добавляем записи в БД по чатам */
INSERT INTO training_base.chat
(chat_name)
VALUES ('Historical chat'),
       ('Physical chat'),
       ('Secret chat'),
       ('Hogwarts chat'),
       ('German chat');

/* Добавляем записи в БД по 'локалям - языкам' */
INSERT INTO training_base.company_locale
(company_id, lang, description)
VALUES (1, 'ENG', 'English language'),
       (2, 'RUS', 'Russian language'),
       (3, 'ENG', 'English language'),
       (4, 'RUS', 'Russian language'),
       (5, 'JAP', 'Japanese language');

/* Добавляем записи в БД по пользователям */
INSERT INTO training_base.users
(first_name, last_name, birth_date, user_name, role, info, company_id)
VALUES ('Tasha', 'Yar', '2645-02-18', 'fighter@enerprise.com', 'ADMIN', '{}', 1),
       ('Jordy', 'LaForge', '2665-02-22', 'vizor@enerprise.com', 'ADMIN', '{}', 1),
       ('Garibo', 'Coply', '2965-03-18', 'garicop@google.com', 'USER', '{}', 1),
       ('Aerdol', 'T-Qute', '1325-07-07', 'aracy@yandex.ru', 'ADMIN', '{}', 2),
       ('Star', 'Lord', '1965-07-21', 'starboy@yahoo.com', 'USER', '{}', 1),
       ('Willy', 'Wonka', '1901-04-15', 'ww@chocolate.com', 'ADMIN', '{}', 3),
       ('Bary', 'Gudkayn', '1465-05-14', 'btk@unyc.com', 'USER', '{}', 1);

/* Добавляем записи в БД по профилям */
INSERT INTO training_base.profile
       (user_id, street, language)
VALUES (2, 'Nejin st. 23', 'RUS'),
       (3, 'Kapitol av. 1234 aprt.42', 'ENG'),
       (4, 'Baus 23', 'FRA'),
       (5, 'TuMany Kort 543', 'ELF'),
       (6, 'Batista st. 1225', 'ENG'),
       (7, 'Cambly Stor st. 532', 'ENG'),
       (8, '1-st st. 231 aprt 34.', 'ENG');

/* Добавляем записи в БД по связной таблице user-chat */
INSERT INTO training_base.users_chats
(user_id, chat_id, created_at, created_by)
VALUES (2, 3, '2023-07-18 11:25:54.931277', 'Uryi'),
       (3, 4, '2023-07-16 12:35:54.931275', 'Katlyn');

