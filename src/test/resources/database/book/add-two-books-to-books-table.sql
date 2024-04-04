INSERT INTO books (id, title, author, isbn, price, description)
VALUES (2, 'Test Title 1', 'Test Author 1', '1234567891', 100, 'Test description 1');
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);

INSERT INTO books (id, title, author, isbn, price, description)
VALUES (3, 'Test Title 2', 'Test Author 2', '3456457896', 100, 'Test description 2');
INSERT INTO books_categories (book_id, category_id) VALUES (3, 1);