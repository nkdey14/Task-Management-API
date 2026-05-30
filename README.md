# Task Management API

A small Spring Boot (Java 17) REST API for managing tasks. This project uses Spring Boot, Spring Data JPA, and an in-memory H2 database for quick development and testing.

Project layout

- src/main/java - application source
  - com.task_management_api.TaskManagementApiApplication - main Spring Boot application class
  - com.task_management_api.controller - REST controllers
  - com.task_management_api.service - service interfaces
  - com.task_management_api.service.impl - service implementations
  - com.task_management_api.repository - Spring Data JPA repositories
  - com.task_management_api.entity - JPA entities
  - com.task_management_api.dto - request/response DTOs
  - com.task_management_api.exception - custom exceptions

Prerequisites

- Java 17 (installed and JAVA_HOME set)
- Maven 3.6+

Build

From the project root (where `pom.xml` is located), run:

mvn clean package -DskipTests=false

This builds the project and runs unit tests.

Run

Run the application with Maven:

mvn spring-boot:run

Or run the packaged jar (after `mvn package`):

java -jar target/task-management-api-0.0.1-SNAPSHOT.jar

By default the app runs on port 8081 (check `application.properties` or environment for overrides).

API

- POST /tasks - create a task
- GET /tasks - list tasks (supports paging and optional status filter)
- GET /tasks/{id} - retrieve task by id
- PUT /tasks/{id} - update task
- DELETE /tasks/{id} - delete task

Tests

Unit tests are located under `src/test/java`. Run them with:

mvn test

Common issues & troubleshooting

- NullPointerException in controller when calling service from controller:
  - Ensure the controller declares the service as a Spring bean and has it injected. Example:
    - Use `@RestController` on controllers and annotate constructor with `@Autowired` or use Lombok's `@RequiredArgsConstructor` and mark the field `private final`.
  - If you see `constructor X is already defined` errors, remove duplicated constructors or Lombok annotations causing duplicate generation.

- Variable '... might not have been initialized' for `private final` field:
  - Ensure you have a constructor that assigns the final field, or use Lombok `@RequiredArgsConstructor` and enable annotation processing in your IDE.

- If you rely on Lombok, make sure your IDE has annotation processing enabled and Lombok plugin installed.

H2 Console

If enabled in `application.properties`, H2 console is often available at `/h2-console`.

Where to look next

- `pom.xml` contains project dependencies and compiler settings (annotation processors for Lombok).
- `src/main/resources/application.properties` for runtime configuration (port, datasource, etc.).

If you want, I can also:
- Add more documentation for the request/response payloads
- Add an OpenAPI/Swagger config file
- Fix the specific controller/service wiring issues that produced the NPE (I noticed your stack trace indicates `taskService` was null in `TaskController`) — I can open relevant controller/service files and patch them.


