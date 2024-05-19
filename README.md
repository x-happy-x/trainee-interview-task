# REST API для РомашкаКо

## Описание
Это REST API проект для работы с ассортиментом товаров компании "РомашкаКо". API позволяет выполнять полный CRUD (Create, Read, Update, Delete) операции с товарами.

## Функциональные требования
- Хранение товаров в базе данных.
- Проверки на ограничения полей:
  - Название товара ограничено 255 символами и обязательно при создании.
  - Описание товара ограничено 4096 символами.
  - Цена товара не может быть меньше 0, по умолчанию 0.
  - Если при создании не указано наличие товара явно, то по умолчанию товар не в наличии.

## Используемые технологии
- Java
- Spring Boot
- Maven
- JUnit5 (для тестирования)
- IntelliJ HTTP Client (для тестирования API)
- PostgreSQL (для хранения данных)

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
  "message": "success",
  "status": 1
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
  "message": "success",
  "status": 1
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
  "message": "success",
  "status": 1
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
  "message": "success",
  "status": 1
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
  "message": "success",
  "status": 1
}
```

## Тестирование
Для тестирования используйте фреймворк JUnit5. Тесты находятся в директории `src/test/java`.

Запуск тестов:
```bash
mvn test
```

## Унифицированные ошибки
При возникновении ошибок API возвращает JSON-объект с описанием ошибки. Пример ответа при неверном запросе:
```json
{
  "message": "Product with id 1 not found",
  "status": -1
}
```
или
```json
{
  "message": "price: Price must be greater than or equal to 0",
  "status": -1
}
```