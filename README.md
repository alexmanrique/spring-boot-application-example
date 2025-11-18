# Spring boot application example

The project is based on a small web service which uses the following technologies:

- Java 1.8
- Spring MVC with Spring Boot
- Database H2 (In-Memory)
- Maven

- The architecture of the web service is built with the following components:
  - DataTransferObjects: Objects which are used for outside communication via the API
  - Controller: Implements the processing logic of the web service, parsing of parameters and validation of in- and outputs.
  - Service: Implements the business logic and handles the access to the DataAccessObjects.
  - DataAccessObjects: Interface for the database. Inserts, updates, deletes and reads objects from the database.
  - DomainObjects: Functional Objects which might be persisted in the database.

# How to start the app

## Prerequisites

- Java 8 or higher (Java 17 tested and working)
- Maven (or use the included Maven Wrapper)

## Build the application

To compile the application, run:

```bash
./mvnw clean compile
```

Or if you have Maven installed globally:

```bash
mvn clean compile
```

## Run the application

### Using Maven Spring Boot plugin (recommended)

```bash
./mvnw spring-boot:run
```

Or with Maven:

```bash
mvn spring-boot:run
```

### Using the compiled JAR

First, build the JAR:

```bash
./mvnw clean package
```

Then run it:

```bash
java -jar target/myapp_server-0.0.1-SNAPSHOT.jar
```

## Access the application

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Base URL**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console (if enabled)

The application starts a web server on port 8080 and serves SwaggerUI where you can inspect and try existing endpoints.

## Notes

- The application is configured to work with Java 9+ (including Java 17) by including necessary JAXB dependencies and JVM arguments.
- Default credentials for basic authentication: `user` / `password`

# Useful commands

Useful curl commands to test. It can be tested with swagger also: http://localhost:8080/swagger-ui.html

```
curl -u user:password "http://localhost:8080/v1/cars"
curl -u user:password "http://localhost:8080/v1/drivers?onlineStatus=ONLINE"
curl -u user:password "http://localhost:8080/v1/drivers?onlineStatus=ONLINE&deleted=false"
curl -u user:password "http://localhost:8080/v1/drivers?onlineStatus=ONLINE&deleted=false&username=driver01"
curl -u user:password -X PUT "http://localhost:8080/v1/drivers/4/car/4545PWR"
```
