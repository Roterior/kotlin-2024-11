# Деплой и запуск

## Делаем деплой

Запускаем файл:\
`docker-compose.yml`

## Получаем токен:

Вызываем курл с Encoded Form Data:\
`curl -X POST \
-d "client_id=otus-gasstation-service" \
-d "username=otus-test" \
-d "password=otus" \
-d "grant_type=password" \
"http://localhost:8080/realms/otus-gasstation/protocol/openid-connect/token"`

## Делаем запрос

Из полученного выше результата запроса достаем токен и вставляем в запрос:\
`curl -H "Authorization: Bearer ${TOKEN}" \
-H "X-Request-ID: 1234" \
-H "x-client-request-id: 1235" \
http://localhost:8080/v1/order/`

## Настройка логов OpenSearch

http://localhost:5601
admin:adm-Password0

Нажать Discover
Нажать Create index pattern
Создаем индексы: app-logs-*, cpu-load-*
Указываем поле времени: @timestamp

Тут можно создать индексы
Management -> Dashboard Management -> Index Patterns
Там же нужно будет обновить индекс поля чтобы можно было фильтровать по полю message

## Адрес Keycloak

http://localhost:8080/admin/master/console
admin:admin
