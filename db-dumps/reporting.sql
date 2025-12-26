CREATE DATABASE IF NOT EXISTS reporting_db;
USE reporting_db;

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    created_at DATETIME NOT NULL
);

INSERT INTO transactions (product_id, type, quantity, created_at) VALUES
(1, 'IN', 10, NOW()),
(1, 'OUT', 2, NOW()),
(2, 'IN', 5, NOW());
