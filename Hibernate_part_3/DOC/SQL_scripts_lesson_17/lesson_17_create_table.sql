/*
Особенности взаимосвязи Table_Per_Class состоит в том,
что для нескольких сущностей создается единая таблица,
например завязанная на родительский класс. Данная единая
таблица содержит 'поле-определитель' в которое помещаются
сведения о том какой сущности принадлежит вся запись.

Естественно данная таблица содержит не только общие поля
всех сущностей (те, что принадлежат родительской сущности),
но и отдельные поля каждой оригинальной сущности. Такой
способ хранения данных может привести к значительному
разрастанию объединенной таблицы, а так же некоторые ее поля
не связанные с конкретной сущностью будут содержат NULL.


*/

/*
Создаем одну таблицу связанную с родительской сущностью Worker 6-ю
общими полями (см. класс oldboy/lesson_18/Entity_18/Worker.java):
- id;
- first_name;
- last_name;
- birth_date;
- employee_email;
- company_id;
два поля связанные с другими дочерними сущностями:
- millwright_specialization (см. oldboy/lesson_18/Entity_18/DesignEngineer.java);
- design_software (см. oldboy/lesson_18/Entity_18/Millwright.java);
а также 'поле-дискриминатор':
- type.
*/
CREATE TABLE training_base.workers
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    worker_email VARCHAR(128) UNIQUE,
    millwright_specialization VARCHAR(128),
    design_software VARCHAR(128),
    type VARCHAR(32) NOT NULL,
    company_id INT REFERENCES training_base.company (company_id)
);
