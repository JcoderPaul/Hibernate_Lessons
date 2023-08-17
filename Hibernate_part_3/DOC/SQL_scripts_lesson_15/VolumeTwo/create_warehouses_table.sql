CREATE TABLE training_base.warehouses
(
  storage_id SERIAL PRIMARY KEY ,
  storage_address VARCHAR(256)
);

CREATE TABLE training_base.distribution_center
(
    center_id BIGSERIAL PRIMARY KEY,
    storage_id BIGINT REFERENCES training_base.warehouses (storage_id),
    driver_id BIGINT REFERENCES training_base.drivers (id),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(128) NOT NULL,
    UNIQUE (storage_id, driver_id)
);