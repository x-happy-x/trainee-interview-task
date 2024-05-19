# Интернет магазин "МоиТовары"

## Описание

Это REST API проект для работы с ассортиментом товаров компании "РомашкаКо".
API позволяет выполнять полный CRUD (Create, Read, Update, Delete) операции с товарами, поставками и продажами.
Теперь ещё включена фильтрация и сортировка товаров

## Функциональные требования

- Использовать Spring Boot
- Покрыть функционал тестами, используя фреймворк JUnit*
- Унифицированные ошибки - реализация json ошибок*
- Хранение товаров, поставок и продаж в СУБД Postgres
- Создание таблиц происходит при запуске приложения, если таких таблиц нет в СУБД*
- Добавлены ограничения на столбцы, в соответствии с бизнес требованиями*
- Использование Docker
- Убран СУБД Postgres в Docker*
- Использование docker-compose*
- Фильтрация и сортировка товаров через параметры запроса
- Валидация значений фильтрации и сортировки
- Фильтры и сортировки применяются вместе*

Не выполнено:
- Покрыть JUnit тестами новые эндпоинты (Поставка товара и Продажа товара)*

## Бизнес требования

- Приложение запускается на различных конфигурациях сервера (ОС, различная конфигурация ОС, различный набор
  вспомогательных программ)
- CRUD для сущности Товар, Поставка товара, Продажа товара
- Проверки на ограничения полей:
    - Товар
        - Название товара ограничено 255 символами и обязательно при создании.
        - Описание товара ограничено 4096 символами.
        - Цена товара не может быть меньше 0, по умолчанию 0.
        - Если при создании не указано наличие товара явно, то по умолчанию товар не в наличии.
    - Продажа товаров
        - Название документа ограниченно 255 символами
        - Товар должен существовать
        - Количество товара в документе должно быть больше 0
        - Нельзя продавать в минус по количеству товара
    - Поставка товаров
        - Название документа ограниченно 255 символами
        - Товар должен быть существующим
        - Количество товара в документе должно быть больше 0
- Фильтровуется:
    - по названию или части названия товара (name)
    - по цене товара, а также больше или меньше заданной цены (price или minPrice и maxPrice)
    - по наличию товара (inStock)
- Сортируется: (sortBy)
    - по имени товара (name)
    - по цене товара (price)
- Можно ограничивать выборку заданным количеством записей (size)*
- В документе “Продажа товара” добавлено поле стоимость покупки *
- В товаре меняется поле в наличии, при изменении количества товара:
    - при поставке - товар появляется в наличии
    - при продаже - товар пропадает из наличия

## Используемые технологии

- Java
- Spring Boot
- Maven
- JUnit5
- IntelliJ HTTP Client
- PostgreSQL
- Docker

## Запустить проект

Запуск приложения:

```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу `http://localhost:8802`.

## Запуск приложения в docker (база отдельно)

Сборка приложения:

```bash
mvn clean package
```

Создание образа:

```bash
docker image build -t trainee-interview-task:latest . 
```

И запуск в контейнере:

```bash
docker run trainee-interview-task:latest
```

## Запуск приложения в docker-compose (с базой данных)

_**Изменить в application.yaml поле spring.datasource.url, заменив там localhost на db**_

Сборка приложения:

```bash
mvn clean package
```

Скачивания нужных образов, сборка и запуск контейнеров:

```bash
docker-compose up --build -d 
```

Если не нужно пересобирать образы, можно упустить --build

```bash
docker-compose up -d 
```

## Тестирование

Для тестирования используйте фреймворк JUnit5. Тесты находятся в директории `src/test/java`.

Запуск тестов:

```bash
mvn test
```

## Эндпоинты

### Получить список товаров

- **URL:** `/products/all`
- **Метод:** `GET`
- **Ответ:**

```json
{
  "response": [
    {
      "id": 1,
      "name": "Продукт",
      "description": "Описание продукта",
      "price": 100.0,
      "inStock": false
    },
    ...
  ],
  "count": 1,
  "status": 200
}
```

### Получить отдельный товар

- **URL:** `/products/{id}`
- **Метод:** `GET`
- **Ответ:**

```json
{
  "response": {
    "id": 1,
    "name": "Продукт",
    "description": "Описание продукта",
    "price": 100.0,
    "inStock": false
  },
  "status": 200
}
```

### Создать товар

- **URL:** `/products/add`
- **Метод:** `POST`
- **Тело запроса:**

```json
{
  "name": "Новый продукт",
  "description": "Описание нового продукта",
  "price": 100.0,
  "inStock": true
}
```

- **Ответ:**

```json
{
  "response": {
    "id": 2,
    "name": "Новый продукт",
    "description": "Описание нового продукта",
    "price": 100.0,
    "inStock": true
  },
  "status": 201
}
```

### Изменить товар

- **URL:** `/products/update`
- **Метод:** `PUT`
- **Тело запроса:**

```json
{
  "id": 1,
  "name": "Обновленный продукт",
  "description": "Описание обновленного продукта",
  "price": 150.0,
  "inStock": false
}
```

- **Ответ:**

```json
{
  "response": {
    "id": 1,
    "name": "Обновленный продукт",
    "description": "Описание обновленного продукта",
    "price": 150.0,
    "inStock": false
  },
  "status": 200
}
```

### Удалить товар

- **URL:** `/products/delete`
- **Метод:** `DELETE`
- **Тело запроса:**

```json
{
  "id": 1
}
```

- **Ответ:**

```json
{
  "status": 200
}
```

## Унифицированные ошибки

При возникновении ошибок API возвращает JSON-объект с описанием ошибки. Пример ответа при неверном запросе:

```json
{
  "path": "/product/1",
  "error": "Product with id 1 not found",
  "status": 404,
  "timestamp": "2024-05-19T16:59:56.231030800"
}
```

или

```json
{
  "path": "/product/update",
  "error": "price: Price must be greater than or equal to 0",
  "status": 400,
  "timestamp": "2024-05-19T17:00:19.117358500"
}
```