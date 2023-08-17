/*
Особенности взаимосвязи Table_Per_Class состоит в том, что
несколько таблиц могут иметь общие поля, а главное сквозной
ID. При этом общие части таблиц и сущностей выделяются в
отдельный класс (сущность), которая НЕ ИМЕЕТ ТАБЛИЦЫ в БД,
а для основных ключей формируется общий SEQUENCE без указания
связующего поля (столбца ID) в таблице БД. Применяется
параметр NONE. См. DOC/SequencesExample.txt
*/

/*
Создаем 2-е таблицы связных с НЕОТАБЛИЧЕННОЙ абстрактной
СУЩНОСТЬЮ Employee 6-ю общими полями
(см. класс oldboy/lesson_16/Entity_16/Employee.java):
- id;
- first_name;
- last_name;
- birth_date;
- employee_email;
- company_id;
и имеющие по одному отличному полю, соответственно, см. классы:
- oldboy/lesson_16/Entity_16/Manager.java;
- oldboy/lesson_16/Entity_16/Programmer.java.
*/
CREATE TABLE training_base.programmers
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    employee_email VARCHAR(128) UNIQUE,
    programming_language VARCHAR(32),
    company_id INT REFERENCES training_base.company (company_id)
);

CREATE TABLE training_base.managers
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    employee_email VARCHAR(128) UNIQUE,
    project_name VARCHAR(256),
    company_id INT REFERENCES training_base.company (company_id)
);
/*
Создаем связывающий эти две таблицы sequence, при этом
у нас НЕТ СПЕЦИАЛЬНОЙ ТАБЛИЦЫ с ПОЛЯМИ ДЛЯ ЭТОЙ
последовательности и мы указываем собственника - NONE.
*/
create sequence training_base.employee_id_seq
    owned by none;