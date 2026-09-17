# Pagination Spring Demo

A small, easy-to-extend example project covering a Spring Boot REST API with pagination, H2, an entity join, request/response logging with Spring AOP (AspectJ annotations), an external HTTP call, unit tests, and a minimal React + TypeScript UI.

## Requirements covered

- JDK 17 + Maven
- HTTP methods: GET, POST, PUT, PATCH, DELETE
- Search with Spring Data pagination and sorting
- Request/response logging via `@Aspect` / `@Around`
- H2 in-memory database
- JPQL queries joining `Employee` and `Department`
- External call to `https://www.google.com` from a service
- Unit/repository/controller tests
- React + TypeScript UI showing search results and Previous/Next pagination
- Postman collection included under `postman/`

## Project structure

```text
src/main/java/com/example/pagination
├── aspect        # request/response AOP logging
├── config        # CORS and sample data
├── controller    # REST endpoints
├── dto           # API contracts/projections
├── entity        # JPA entities
├── exception     # error handling
├── repository    # Spring Data + join queries
└── service       # business logic + external API call
ui/               # Vite + React + TypeScript
postman/          # Postman collection
```

## Run backend

Use JDK 17 and Maven 3.9+:

```bash
mvn spring-boot:run
```

Backend: `http://localhost:8080`

H2 console: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:paginationdb`
- User: `sa`
- Password: empty

## Run tests

```bash
mvn test
```

## Run React UI

```bash
cd ui
npm install
npm run dev
```

Open `http://localhost:5173`.

## Main API examples

### Paginated search

```http
GET /api/employees/search?keyword=engineer&department=Engineering&page=0&size=5&sortBy=name&direction=asc
```

The repository query joins the two tables:

```java
select e
from Employee e
join e.department d
where ...
```

### CRUD / different HTTP methods

```text
GET    /api/employees/{id}
POST   /api/employees
PUT    /api/employees/{id}
PATCH  /api/employees/{id}
DELETE /api/employees/{id}
```

### Explicit join/aggregate example

```http
GET /api/employees/department-summary
```

This executes a grouped JPQL join between employees and departments.

### External API call

```http
GET /api/integration/google-status
```

The controller delegates to `ExternalApiService`, which uses JDK `HttpClient` to call Google.

## Logging

`RequestResponseLoggingAspect` intercepts controller methods and logs:

- HTTP method and URI
- controller handler
- method arguments/body
- response payload
- execution duration
- exceptions

For a production service, sensitive fields should be masked before logging and large bodies should normally be truncated.

## Postman

Import:

```text
postman/Pagination-Spring-Demo.postman_collection.json
```

The collection uses `{{baseUrl}}`, defaulting to `http://localhost:8080`.
