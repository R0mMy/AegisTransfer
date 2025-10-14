# AegisTransfer

Мини-сервис для управления банковскими счетами (создание, просмотр, пополнение/списание, закрытие) на Spring Boot.

## Стек

- Java **21**
- Spring Boot **3.5.6**: Web, Data JPA, Validation, (Security присутствует в зависимостях, но без явной конфигурации)
- Liquibase (миграции)
- PostgreSQL (JDBC-драйвер)
- Lombok
- Maven Wrapper (`mvnw`/`mvnw.cmd`)

В репозитории есть заглушки под Redis/Kafka/OpenAPI, но в текущем коде они **не используются**.

## Возможности

- **Создание счёта**  
  `POST /api/accounts/create` — приём `AccountRequest` с валидацией:
  - `clientId` — `@NotBlank`
  - `initialBalance` — `@DecimalMin(value="0.0", inclusive=false)`
  - `currency` — `@NotBlank`

- **Получение счёта по ID**  
  `GET /api/accounts/{id}` — возвращает `AccountResponse`.

- **Список счетов клиента**  
  `GET /api/accounts/client/{clientId}` — массив `AccountResponse` по `clientId`.

- **Пополнение счёта**  
  `POST /api/accounts/{id}/deposit?amount=...` — увеличивает баланс, возвращает актуальный `AccountResponse`.

- **Списание средств**  
  `POST /api/accounts/{id}/withdraw?amount=...` — уменьшает баланс при достаточном остатке, возвращает `AccountResponse`.

- **Закрытие счёта**  
  `DELETE /api/accounts/{id}/close` — помечает счёт закрытым (см. статусы).

- **Бизнес-валидация на сервисе**
  - Проверка существования счёта (`AccountNotFoundException`).
  - Проверка статуса (активен/заблокирован/закрыт) — при попытке операции по заблокированному/закрытому счёту выбрасывается `AccountBlockedException`.
  - Проверка достаточности средств при списании (`hasSufficientFunds(...)`).

> Обработчик ошибок (`@ControllerAdvice`) пока не реализован — исключения падают стандартным JSON Spring Boot.


## Модель данных

### `Account` (JPA)

| Поле        | Тип            | Колонка БД   |
|-------------|-----------------|--------------|
| `id`        | `long`          | `id` (PK)    |
| `clientId`  | `String`        | `client_id`  |
| `balance`   | `BigDecimal`    | `balance`    |
| `currency`  | `String`        | `currency`   |
| `status`    | `AccountStatus` | `status`     |
| `createdAt` | `LocalDateTime` | `created_at` |

`AccountStatus`: `ACTIVE`, `BLOCKED`, `CLOSED`.

> **Важно:** в Liquibase-миграции у `status` выставлен `defaultValue="PENDING"`, тогда как такого значения **нет** в enum’е. Рекомендуется:
> - либо поменять default в миграции на `ACTIVE`,
> - либо добавить `PENDING` в `AccountStatus` и/или устанавливать статус программно при создании счёта.

### Liquibase

`db/changelog/changes/001-create-initial-tables.xml` создаёт таблицу `accounts` с полями:
`id`, `client_id`, `balance`, `currency`, `status` (default `PENDING`), `created_at` (default `CURRENT_TIMESTAMP`), `idempotency_key` (UNIQUE).

> В `Account` сейчас нет поля `idempotencyKey` — если нужна идемпотентность, синхронизируйте сущность и миграцию.

## DTO

- **`AccountRequest`**  
  ```json
  {
    "clientId": "string",
    "initialBalance": 100.00,
    "currency": "RUB"
  }

AccountResponse

{
  "id": 1,
  "clientId": "string",
  "balance": 100.00,
  "currency": "RUB",
  "status": "ACTIVE"
}



ДОПОЛНЕНИЕ:
REST API

Базовый префикс: /api/accounts

Создать счёт

POST /api/accounts/create
Body: AccountRequest
200 OK ⇒ AccountResponse

curl -X POST http://localhost:8080/api/accounts/create \
  -H "Content-Type: application/json" \
  -d '{"clientId":"c-001","initialBalance":5000,"currency":"RUB"}'

Получить счёт по ID

GET /api/accounts/{id} → AccountResponse

curl http://localhost:8080/api/accounts/1

Все счета клиента

GET /api/accounts/client/{clientId} → [AccountResponse]

curl http://localhost:8080/api/accounts/client/c-001

Пополнение

POST /api/accounts/{id}/deposit?amount=123.45 → AccountResponse

curl -X POST "http://localhost:8080/api/accounts/1/deposit?amount=123.45"

Списание

POST /api/accounts/{id}/withdraw?amount=50 → AccountResponse

curl -X POST "http://localhost:8080/api/accounts/1/withdraw?amount=50"

Закрыть счёт

DELETE /api/accounts/{id}/close → AccountResponse (с обновлённым статусом)

curl -X DELETE http://localhost:8080/api/accounts/1/close

Конфигурация и запуск
1) Настройка БД / окружения

Есть шаблон: src/main/resources/application-template.properties
Создайте application.properties на его основе и заполните креды PostgreSQL:

spring.datasource.url=jdbc:postgresql://localhost:5432/aegis_transfer
spring.datasource.username=POSTGRES_USERNAME
spring.datasource.password=POSTGRES_PASSWORD

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/main-changelog.xml

spring.application.name=AegisTransfer
server.port=8080


Можно использовать .env.example (docker-compose подхватит POSTGRES_USERNAME/POSTGRES_PASSWORD).

2) Локальная БД через docker-compose (опционально)

docker-compose.yml поднимает Postgres (5433→5432), Redis, ZooKeeper, Kafka (для будущего). Для текущего сервиса нужен только Postgres.

docker compose up -d postgres
# БД будет доступна на jdbc:postgresql://localhost:5433/aegis_transfer


Если используете именно compose-порт 5433, поправьте spring.datasource.url на jdbc:postgresql://localhost:5433/aegis_transfer.

3) Сборка и запуск приложения
./mvnw clean package
java -jar target/AegisTransfer-0.0.1-SNAPSHOT.jar
# или
./mvnw spring-boot:run

Поведение ошибок

Отсутствие счёта → в AccountService.validate(...) выбрасывается AccountNotFoundException("Счёт не найден!").

Счёт заблокирован/закрыт → AccountBlockedException("Счёт заблокирован или закрыт").

Недостаточно средств → проверка hasSufficientFunds(...) (исключение/код ответа зависит от фактической реализации; централизованного @ControllerAdvice нет).

Рекомендуется добавить глобальный обработчик ошибок, чтобы возвращать консистентные 4xx/5xx с понятным JSON.

Тесты

AegisTransferApplicationTests — smoke-тест контекста Spring (contextLoads()).

Roadmap / TODO

Синхронизировать enum AccountStatus и default в Liquibase (PENDING vs ACTIVE).

Добавить поле idempotencyKey в Account (или убрать его из миграции).

Реализовать домен транзакций (пакет transaction), историю операций, идемпотентность.

Удалить неиспользуемые зависимости (Security/Thymeleaf) или добавить их конфигурацию/шаблоны.

OpenAPI/Swagger (класс OpenApiConfig сейчас пустой).

Конфигурация безопасности (сейчас действует дефолт Spring Security).

Интеграция с Kafka/Redis (при необходимости) и docker-окружение для них.

Нормальный Dockerfile для приложения (текущий — заглушка).
