CREATE TABLE public.book_locale
(
    book_id INT NOT NULL REFERENCES books (book_id),
    lang CHAR(2) NOT NULL,
    description VARCHAR (128) NOT NULL,
    PRIMARY KEY (book_id, lang)
);