# ShareIt

### Описание приложения

Сервис позволяет бронировать вещи на определённые даты c мониторингом доступности вещи в определенный период времени.
У пользователей есть возможность оставлять запросы, по которому можно будет добавлять новые вещи для шеринга.
Доступны все CRUD операции с сущностями приложения.

### Технологии
Приложение запускается в трех докер контейнерах. Первый shareit-gateway - шлюз, отвечающий за валидацию всех
входных параметров запроса и при успешной валидации перенаправляет запрос с помощью RestTemplate в сервис второго контейнера shareit-server, 
где находится основная бизнес логика приложения. Третий контейнер содерит в себе базу данных PostgreSQL.

### ER диаграмма

![image](server/src/main/resources/ER-diagram.png)

