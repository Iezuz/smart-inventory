Проект: микросервисная система учёта склада (Inventory, Transactions, Reporting) + API Gateway + фронтенд (Vue).

Краткое описание архитектуры:

- inventory-service — отвечает за сущности товаров и их текущие остатки (источник правды по stock).

- transaction-service — хранит историю приход/расход (IN/OUT). При создании транзакции сначала вызывает Inventory (decrease/increase), и только при успешном ответе сохраняет запись.

- reporting-service — read-model / агрегаты / отчёты (движения, сводка).

- gateway-service — Spring Cloud Gateway (маршрутизация).

- frontend — Vue.

- mysql — единый MySQL-контейнер в compose. Базы: inventory_db, transactions_db, reporting_db.


Архитектура проекта:


    Frontend
        |
        v
    API Gateway (Spring Boot)
        |
    ------------------------------------------------
    |                    |                         |
    Inventory Service   Transaction Service   Reporting Service
    |                    |                         |
    MySQL (inventory)   MySQL (transactions)   MySQL (reporting)


Базы данных

Каждый сервис использует собственную базу данных:

    Сервис	             Контейнер БД	База данных	    Порт
    Inventory Service	inventory-db	inventory_db	3306
    Transaction Service	transactions-db	transactions_db	3307
    Reporting Service	reporting-db	reporting_db	3308

Все дампы находятся в папке db-dumps/

