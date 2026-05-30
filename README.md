# Task Management API

A complete, end-to-end Task Management REST API built with Spring Boot (Java 17).

This document explains the project purpose, architecture, features implemented, how to build/run/test it locally, configuration notes, and a concise API reference. At the end you'll find a link to the project's Postman documentation for full request/response examples.

---

## Table of contents

- Project overview
- Architecture & key components
- Features implemented
- Project structure
- Data model and DTOs
- Configuration
- Build, run, test (commands)
- Common troubleshooting
- API endpoints (summary)
- Postman / API documentation link

---

## Project overview

The Task Management API provides endpoints to create, read, update, delete, and list tasks with pagination and status filtering. It is a simple microservice-style REST API suitable for learning Spring Boot, JPA, validation, and testing.

Primary goals:
- Provide a clean, well-structured Spring Boot service.
- Demonstrate controller -> service -> repository layering.
- Use DTOs for requests/responses and entity mapping.
- Use an in-memory H2 database for easy local development and testing.
- Include unit tests and common validation/error handling.

---

## Architecture & key components

- Spring Boot (starter parent) as the application framework.
- Spring Web for REST controllers.
- Spring Data JPA for persistence.
- H2 in-memory database for local development and tests.
- Validation via `spring-boot-starter-validation`.
- Lombok used to reduce boilerplate (constructor/getters/setters) — annotation processing enabled in build.
- Typical layered architecture:
  - Controller: request mapping and HTTP handling.
  - Service: business logic.
  - Repository: JPA repositories and data access.
  - DTOs: request/response shapes separate from entities.

---

## Features implemented

- Create a task (POST /tasks).
- Read a single task by id (GET /tasks/{id}).
- Update a task (PUT /tasks/{id}).
- Delete a task (DELETE /tasks/{id}).
- List tasks with pagination and optional status filter (GET /tasks?page=0&size=10&status=OPEN).
- Validation on incoming request payloads (`@Valid`).
- Clear separation of DTOs and entities.
- In-memory H2 database preconfigured for development.
- Unit test dependencies (Spring Boot test and Mockito) are included.

---

## Project structure (key files/folders)

- `src/main/java/com/task_management_api/`
  - `TaskManagementApiApplication.java` - main Spring Boot application class
  - `controller/` - REST controllers (e.g. `TaskController`)
  - `service/` - service interfaces
  - `service/impl/` - service implementations
  - `repository/` - Spring Data JPA repositories
  - `entity/` - JPA entity classes
  - `dto/` - request and response DTOs
  - `exception/` - custom exceptions and handlers
- `src/main/resources/application.properties` - runtime configuration
- `pom.xml` - maven configuration, dependencies, compiler settings

---

## Data model & DTOs (summary)

This project uses a `Task` entity (persisted) and separate DTOs to shape API input/output. Typical fields:

- Task entity
  - id (String/UUID)
  - title (String)
  - description (String)
  - status (enum: e.g. OPEN, IN_PROGRESS, DONE)
  - createdAt, updatedAt (timestamps)

- TaskRequest (DTO for create/update)
  - title (required)
  - description (optional)
  - status (optional for create, required for update)

- TaskResponse (DTO returned to clients)
  - id, title, description, status, createdAt, updatedAt

(Exact property names/types can be found in `src/main/java/com/task_management_api/entity` and `dto`.)

---

## Configuration

The application uses sensible defaults for development. Check `src/main/resources/application.properties` for overrides. Key configuration items you may want to change:

- Server port: default may be `8081` in this repo — search `server.port` in `application.properties`.
- H2 console: enable if you want a browser UI to inspect the database (endpoint is typically `/h2-console`).

Environment variables (optional):
- `SERVER_PORT` or `server.port` property to change HTTP port.

---

## Build, run, test

Prerequisites:
- Java 17 installed and `JAVA_HOME` configured.
- Maven 3.6+

From the project root (where `pom.xml` is located) you can build, run, and test the project using these commands.

- Build and run unit tests:

```powershell
mvn clean package
```

- Run the application using Maven:

```powershell
mvn spring-boot:run
```

- Run the compiled jar after packaging:

```powershell
java -jar target/task-management-api-0.0.1-SNAPSHOT.jar
```

- Run only tests:

```powershell
mvn test
```

Notes:
- If Lombok-generated constructors are used, ensure your IDE has annotation processing enabled to avoid "variable might not have been initialized" warnings in the IDE. The build itself includes annotation processing configuration so Maven will compile fine.

---

## Common troubleshooting

- NullPointerException where a controller's service field is null
  - Ensure controller is a Spring bean (`@RestController`) and the service is injected via constructor or `@Autowired`. If you use `private final TaskService taskService`, you must provide a constructor that assigns it (or use Lombok `@RequiredArgsConstructor`).
  - If you see "constructor is already defined" errors, you may have both a hand-written constructor and Lombok generating one. Remove duplicates — either keep the manual constructor or the Lombok annotation.

- Duplicate dependency warnings in `pom.xml`
  - Remove duplicate dependency entries; this is a warning but not fatal.

- H2 console or datasource issues
  - Check `application.properties` datasource settings and whether H2 is included in `pom.xml` (it is by default for this project).

If you want, I can open and fix specific files that produce runtime errors (for example, wiring issues where `taskService` is null). I can run quick edits and run unit tests locally in the workspace.

---

## API endpoints — quick reference

All endpoints are rooted under `/tasks`.

- POST /tasks
  - Create a new task
  - Body: `TaskRequest` JSON (title required)
  - Response: 201 Created with `TaskResponse` body

- GET /tasks
  - List tasks
  - Query params:
    - `page` (default 0)
    - `size` (default 10)
    - `status` (optional filter by TaskStatus)
  - Response: 200 OK with a paged list of `TaskResponse` objects

- GET /tasks/{id}
  - Retrieve a single task by id
  - Response: 200 OK with `TaskResponse` or 404 if not found

- PUT /tasks/{id}
  - Update an existing task
  - Body: `TaskRequest` JSON
  - Response: 200 OK with updated `TaskResponse`

- DELETE /tasks/{id}
  - Delete a task by id
  - Response: 204 No Content on success

Notes on validation:
- Request payloads use `@Valid` annotations; invalid input returns 400 with a validation message.

---

## Postman / API documentation

Full request/response examples and a collection are available at the Postman documentation linked below:

https://documenter.getpostman.com/view/23709764/2sBXwntXNp

You can import the collection into Postman via that link for ready-to-run examples.

---

## Next steps / suggestions

- Add OpenAPI/Swagger (springdoc-openapi) to generate interactive API docs.
- Add integration tests with MockMvc or Testcontainers for a more representative environment.
- Add authentication/authorization if the API should be private.

---

If you'd like, I can now:
- Add detailed example payloads for every endpoint in this README.
- Generate an OpenAPI spec or Swagger UI and wire it into this project.
- Inspect and fix the `NullPointerException` you saw earlier by reviewing `TaskController` and `TaskServiceImpl` and running the tests.

Tell me which of these you'd like me to pick next and I'll proceed.
