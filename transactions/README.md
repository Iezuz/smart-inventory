# Transactions Service — SmartInventory

Микросервис для регистрации складских операций.

## Эндпоинты
- GET /transactions — получить все операции
- GET /transactions/{id} — получить операцию по ID
- POST /transactions — создать операцию
  - Пример тела:
    ```json
    {
      "productId": 1,
      "type": "IN",
      "quantity": 5
    }
    ```
- DELETE /transactions/{id} — удалить операцию

