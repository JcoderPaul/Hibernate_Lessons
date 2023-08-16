CREATE TABLE IF NOT EXISTS public.students
(
    id bigserial NOT NULL,
    student_name varchar(256) unique,
    student_email varchar(128),
    age integer,
    PRIMARY KEY (id)
);

/* ������� sequence - ������� */
create sequence student_id_seq
owned by students.id;