-- Verificar e criar a base de dados 'books_1' se não existir
SELECT 'CREATE DATABASE books_1'
    WHERE NOT EXISTS (SELECT FROM pg_catalog.pg_database WHERE datname = 'books_1')
\gexec;

-- Verificar e criar a base de dados 'books_2' se não existir
SELECT 'CREATE DATABASE books_2'
    WHERE NOT EXISTS (SELECT FROM pg_catalog.pg_database WHERE datname = 'books_2')
\gexec;
