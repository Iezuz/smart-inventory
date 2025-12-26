CREATE DATABASE IF NOT EXISTS inventory_db;

USE inventory_db;

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(50),
    category VARCHAR(50),
    quantity INT DEFAULT 0
);

INSERT INTO products (name, sku, category, quantity) VALUES
('Товар A', 'SKU001', 'Категория 1', 10),
('Товар B', 'SKU002', 'Категория 2', 5);
