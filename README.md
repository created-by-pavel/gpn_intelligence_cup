# GPN_INTELLIGENCE_CUP 2022

## Описание решения:
Стэк: Spring Boot, Spring Cloud, vk-java-sdk, PostgreSQL, zipkin, Docker  

Документация: http://localhost:8081/webjars/swagger-ui/index.html  
Docker-Hub: https://hub.docker.com/u/crbpavel  


## Описание задания

Необходимо разработать интеграционный RESTful cloud-native сервис на Java 
для получуния ФИО пользователя VK, а также признака участника группы VK. Для работы с VK 
следует использовать API ВКонтакте.

### Техническое описание сервиса:
Сервис должен реализовывать REST-метод на вход 
принимающий HTTP заголовок vk_service_token и тело запроса в формате JSON вида:
```json
{
  "user_id": "123456",
  "group_id": "654321"
}
```
Где:
* user_id - идентификатор пользователя VK
* group_id - идентификатор группы VK
* vk_service_token - сервисный ключ приложения VK используемый для доступа к API ВКонтакте

В ответ должно приходить тело ответа в формате JSON вида:
```json
{
  "last_name": "Иванов",
  "first_name": "Иван",
  "middle_name": "Иванович",
  "member": true
}
```
Где:
* last_name - фамилия пользователя user_id
* first_name - имя пользователя user_id
* last_name - отчество пользователя user_id
* member - признак того, что пользователь состоит в группе group_id

В приложении должен быть реализованы:
* Валидация запросов на предмет корретности формата и передаваемых полей
* Обработка ошибок VK с выдачей информативного ответа
* Swagger или OpenApi документация
* JUnit тесты

ДОПОЛНИТЕЛЬНО:
- [x] Своя авторизация для API нашего приложения (BASIC или Oauth)
- [x] Кеширование ответов на стороне нашего приложения
- [ ] Сборка и деплой приложения в облако (например локально в minikube/minishift)
  - [x] сборка
  - [ ] деплой