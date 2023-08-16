CREATE TABLE IF NOT EXISTS public.books
(
    book_id INT PRIMARY KEY,
    book_name VARCHAR(128) NOT NULL UNIQUE
);