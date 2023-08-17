/*
Особенности взаимосвязи Joined состоит в том, что каждая
сущность при таком типе наследования (взаимосвязи) обладает
своей таблицей, которая будет содержать только поля данной
конкретной сущности. Связь осуществляется по основному ключу
родительской таблицы и основным-и-одновременно-внешним ключам
таблиц наследников.

См. DOC/InheritanceOptions.jpg
*/

CREATE TABLE training_base.service_specialist
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    birth_date DATE,
    specialist_email VARCHAR(128) UNIQUE,
    company_id INT REFERENCES training_base.company (company_id)
);

CREATE TABLE training_base.engine_repairman
(
    id BIGSERIAL PRIMARY KEY REFERENCES training_base.service_specialist (id),
    engine_model VARCHAR(128)
);

CREATE TABLE training_base.tinman
(
    id BIGSERIAL PRIMARY KEY REFERENCES training_base.service_specialist (id),
    machine_body_detail VARCHAR(256)
);
