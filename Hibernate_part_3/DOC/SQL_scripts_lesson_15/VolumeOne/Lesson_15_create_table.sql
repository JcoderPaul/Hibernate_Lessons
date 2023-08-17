CREATE TABLE training_base.drivers
(
  id SERIAL PRIMARY KEY ,
  driver_name VARCHAR(256) ,
  driver_age INTEGER ,
  car_model VARCHAR(128)
);

CREATE TABLE training_base.porters
(
    id BIGSERIAL PRIMARY KEY ,
    porter_name VARCHAR(256) ,
    porter_age INTEGER ,
    storage_number INTEGER
);