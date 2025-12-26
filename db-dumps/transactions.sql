CREATE DATABASE IF NOT EXISTS transactions_db;
USE transactions_db;

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    created_at DATETIME NOT NULL
);

INSERT INTO transactions (product_id, type, quantity, created_at) VALUES
(1, 'IN', 10, NOW()),
(2, 'OUT', 3, NOW());
