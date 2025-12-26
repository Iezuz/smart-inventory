# Reporting Service — SmartInventory

Микросервис формирования простых отчётов на основе записей о движениях товаров.

## Эндпоинты
- GET /reports/movement?from=2025-01-01&to=2025-12-31 — получить движения за период
- GET /reports/stock-summary — суммарные остатки по productId (IN - OUT)
- GET /reports/stock-low?threshold=5 — продукты с остатком ниже порога
