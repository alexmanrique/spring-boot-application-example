# Spring boot application example

The project is based on a small web service which uses the following technologies:

* Java 1.8
* Spring MVC with Spring Boot
* Database H2 (In-Memory)
* Maven

 * The architecture of the web service is built with the following components:
   * DataTransferObjects: Objects which are used for outside communication via the API
   * Controller: Implements the processing logic of the web service, parsing of parameters and validation of in- and outputs.
   * Service: Implements the business logic and handles the access to the DataAccessObjects.
   * DataAccessObjects: Interface for the database. Inserts, updates, deletes and reads objects from the database.
   * DomainObjects: Functional Objects which might be persisted in the database.

# How to start the app
You should be able to start the example application by executing com.myapp.MyappServer, which starts a webserver on port 8080 (http://localhost:8080) and serves SwaggerUI where can inspect and try existing endpoints.

# Useful commands
Useful curl commands to test. It can be tested with swagger also: http://localhost:8080/swagger-ui.html

```
curl -u user:password "http://localhost:8080/v1/cars"
curl -u user:password "http://localhost:8080/v1/drivers?onlineStatus=ONLINE"
curl -u user:password "http://localhost:8080/v1/drivers?onlineStatus=ONLINE&deleted=false"
curl -u user:password "http://localhost:8080/v1/drivers?onlineStatus=ONLINE&deleted=false&username=driver01"
curl -u user:password -X PUT "http://localhost:8080/v1/drivers/4/car/4545PWR"
```
