/*
Как уже говорилось особенности Ehcache зависят от версии, в нашей
почему-то при кэшировании ему не приглянулись поля таблиц и поля
сущностей. Возможно я недопонял, однако у нас два варианта, изменить
поля на которые ругается провайдер кэша в сущностях или в базе данных,
оба варианта работают (String на Character или Char на Varchar(256))
*/

ALTER TABLE public.company_locale
ALTER COLUMN lang TYPE character varying(256) COLLATE pg_catalog."default";

ALTER TABLE public.profile
    ALTER COLUMN language TYPE character varying(256) COLLATE pg_catalog."default";