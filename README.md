# Task Management API

A complete, end-to-end Task Management REST API built with Spring Boot (Java 17).

This document explains the project purpose, architecture, features implemented, how to build/run/test it locally, configuration notes, and a concise API reference. At the end you'll find the Postman API documentation link for full request/response examples.

---

## Table of contents

- Project overview
- Architecture & key components
- Features implemented
- Project structure
- Data model and DTOs
- Configuration
- Build, run, test (commands)
- Testing strategy (unit + integration)
- Common troubleshooting
- API endpoints (summary)
- Postman / API documentation link

---

## Project overview

The Task Management API provides REST endpoints to create, read, update, delete, and list tasks with pagination and status filtering. The project is designed as a small microservice and demonstrates:

- Layered architecture (Controller -> Service -> Repository)
- DTOs for request/response separation from persistence models
- Validation and global error handling
- Persistence with Spring Data JPA and H2 for local/integration tests
- Unit tests (Mockito + JUnit 5) and integration tests (Spring Boot Test + MockMvc)

This repository is intended for learning, quick prototyping and as a base to extend with authentication, file uploads, or additional business rules.

---

## Architecture & key components

- Spring Boot as the application framework
- Spring Web for REST controllers
- Spring Data JPA for data access
- H2 in-memory database for local development & tests
- Validation via `spring-boot-starter-validation` (Jakarta Validation)
- Lombok (optional) for reduced boilerplate

Layering:
- Controller: HTTP mapping and request/response handling (`/tasks` endpoints)
- Service: business logic and mapping between DTOs and entities
- Repository: JPA repository interfaces for persistence
- DTOs: `TaskRequest` and `TaskResponse` for input/output shapes

---

## Features implemented

- Create a task (POST /tasks)
- Read a single task by id (GET /tasks/{id})
- Update a task (PUT /tasks/{id})
- Delete a task (DELETE /tasks/{id})
- List tasks with pagination and optional status filter (GET /tasks?page=0&size=10&status=OPEN)
- Validation on incoming request payloads (`@Valid`) with a global exception handler
- In-memory H2 database for development and tests
- Unit tests and integration tests scaffolded using JUnit, Mockito and Spring Boot Test

---

## Project structure (key files/folders)

- `src/main/java/com/task_management_api/`
  - `TaskManagementApiApplication.java` - main application class
  - `controller/` - REST controllers (e.g. `TaskController`)
  - `service/` - service interfaces
  - `service/impl/` - service implementations (business logic)
  - `repository/` - Spring Data JPA repositories
  - `entity/` - JPA entity classes (Task, TaskStatus)
  - `dto/` - request and response DTOs (`TaskRequest`, `TaskResponse`)
  - `exception/` - custom exceptions and global exception handler
- `src/test/java/` - unit and integration tests
- `pom.xml` - maven configuration and dependencies

---

## Data model & DTOs (summary)

The project uses a `Task` JPA entity and DTOs for API contracts.

- Task entity
  - id (String, generated UUID)
  - title (String, required)
  - description (String, optional)
  - status (enum: PENDING, IN_PROGRESS, DONE)
  - dueDate (LocalDate, required)

- TaskRequest (DTO used for create/update)
  - title (required)
  - description (optional)
  - status (optional; defaults to PENDING)
  - dueDate (required; must be a future date)

- TaskResponse (DTO returned to clients)
  - id, title, description, status, dueDate

Exact property details are in `src/main/java/com/task_management_api/entity` and `src/main/java/com/task_management_api/dto`.

---

## Configuration

Check `src/main/resources/application.properties` for runtime settings. H2 is included as a runtime dependency and is used in tests by default.

Key properties you might care about:
- `server.port` — change the HTTP port if needed
- H2 console settings — enable if you want to inspect DB in browser

---

## Build, run, test

Prerequisites: Java 17 and Maven.

From the project root (where `pom.xml` is located):

- Build and run unit tests:

```powershell
mvn clean package
```

- Run the application using Maven:

```powershell
mvn spring-boot:run
```

- Run the packaged jar:

```powershell
java -jar target/task-management-api-0.0.1-SNAPSHOT.jar
```

- Run only tests:

```powershell
mvn test
```

Notes:
- Use `-Dspring.profiles.active=test` to force the `test` profile when running tests with profile-specific properties.

---

## Testing strategy (implemented in this repo)

This project follows Test-Driven Development (TDD) principles and includes both unit and integration tests.

- Tools used: JUnit 5, Mockito, Spring Boot Test, MockMvc
- Unit tests:
  - Service layer tests mock repository dependencies using Mockito to isolate business logic.
  - DTO/validation tests use Jakarta Validator to ensure `@Valid` constraints behave as expected.
  - Controller slice tests use `@WebMvcTest` and `@MockBean` to mock the service layer and verify HTTP contract and input validation.
- Integration tests:
  - Use `@SpringBootTest` + `@AutoConfigureMockMvc` to run the application context with an in-memory H2 DB and exercise the full stack via MockMvc.
  - Tests cover full CRUD flow and expected HTTP status codes and responses.

Run tests with:

```powershell
mvn test
```

Or run a specific test class:

```powershell
mvn -Dtest=TaskServiceImplTest test
```

---

## Common troubleshooting & known issues

- NullPointerException where a controller's service field is null
  - Ensure controller is annotated with `@RestController` and that it uses constructor injection for `final` dependencies. Avoid defining duplicate constructors (for example a manual constructor and Lombok-generated one) — the compiler error "constructor is already defined" indicates duplicate constructors exist.

- Duplicate dependency entries in `pom.xml`
  - Remove duplicate dependencies (e.g., `spring-boot-starter-test` appeared twice). This is usually harmless for compilation but makes the POM noisy.

- Test failures that depend on time-sensitive data (dueDate):
  - Use relative dates (LocalDate.now().plusDays(...)) in tests to avoid brittleness.

---

## API endpoints — quick reference

Base path: `/tasks`

- POST /tasks
  - Create a new task
  - Body: `TaskRequest` JSON (title required)
  - Response: 201 Created with `TaskResponse` body

- GET /tasks
  - List tasks with pagination & optional status filter
  - Query params: `page` (default 0), `size` (default 10), `status` (optional)
  - Response: 200 OK with a paged list of `TaskResponse` objects

- GET /tasks/{id}
  - Get a single task by id
  - Response: 200 OK with `TaskResponse` or 404 Not Found

- PUT /tasks/{id}
  - Update an existing task
  - Body: `TaskRequest`
  - Response: 200 OK with updated `TaskResponse`

- DELETE /tasks/{id}
  - Delete a task
  - Response: 204 No Content on success

Validation errors return 400 with a map of field -> message. Resource not found returns 404 with a JSON error body containing timestamp, status, and message.

---

## Postman / API documentation

Full request/response examples and a collection are available at the Postman documentation linked below. Import it into Postman to try the API quickly.

Postman doc: https://documenter.getpostman.com/view/23709764/2sBXwntXNp

---

## Next steps / suggestions

- Add OpenAPI (springdoc-openapi) to generate interactive docs.
- Add Testcontainers for integration tests to run against a real database (Postgres) in CI.
- Add authorization if the API will be exposed publicly.

If you want, I can now:
- Add example payloads for every endpoint inside this README.
- Create CI pipeline steps (GitHub Actions) to run tests and build.
- Fix specific runtime errors you've seen (for example wiring issues in controllers) and run the test suite locally.
