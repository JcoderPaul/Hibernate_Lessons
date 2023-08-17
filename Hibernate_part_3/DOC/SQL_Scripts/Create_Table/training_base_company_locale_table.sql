CREATE TABLE training_base.company_locale
(
    company_id INT NOT NULL REFERENCES training_base.company (company_id),
    lang CHAR(2) NOT NULL,
    description VARCHAR(128) NOT NULL,
    PRIMARY KEY (company_id, lang)
);