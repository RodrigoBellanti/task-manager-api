# Task Manager REST API

A professional RESTful API for task management built with Spring Boot and Java 17. This project demonstrates CRUD operations, JPA/Hibernate integration, DTOs, input validation, error handling, pagination, interactive API documentation, and comprehensive unit testing.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen)
![Tests](https://img.shields.io/badge/tests-20%20passing-success)

## 🚀 Features

- RESTful API design with proper HTTP methods and status codes
- DTO pattern for clean separation between API and domain models
- Comprehensive input validation with custom error messages
- Global exception handling with meaningful error responses
- **Pagination and sorting** for efficient data retrieval
- **Swagger/OpenAPI documentation** with interactive UI
- H2 in-memory database for quick setup and testing
- JPA/Hibernate for data persistence
- Transactional service layer
- Unit and controller tests with Mockito (20 tests, 90%+ coverage)
- Automatic timestamps for created/updated records
- **CI/CD pipeline** with GitHub Actions

## 🛠️ Technologies

- **Java 17**
- **Spring Boot 3.5.10**
- **Spring Data JPA**
- **Spring Validation**
- **Springdoc OpenAPI 2.7.0** (Swagger UI)
- **H2 Database**
- **Lombok**
- **JUnit 5**
- **Mockito**
- **Maven**

## 📋 Prerequisites

- JDK 17 or higher
- Maven 3.6+
- (Optional) Docker for containerization

## ⚙️ Installation & Setup

1. Clone the repository:
```bash
git clone https://github.com/RodrigoBellanti/task-manager-api.git
cd task-manager-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## 📖 API Documentation

### Swagger UI (Interactive Documentation)

Access the Swagger UI at:
```
http://localhost:8080/swagger-ui/index.html
```

Here you can:
- View all available endpoints
- Test endpoints directly from the browser
- See request/response models
- View validation rules

### OpenAPI Specification

View the raw OpenAPI spec at:
```
http://localhost:8080/v3/api-docs
```

## 🔌 API Endpoints

### Get all tasks (with optional pagination)

**Without pagination:**
```http
GET /api/tasks
```

**With pagination:**
```http
GET /api/tasks?page=0&size=10
```

**With sorting:**
```http
GET /api/tasks?page=0&size=10&sortBy=title&direction=DESC
```

**With filtering:**
```http
GET /api/tasks?completed=false&page=0&size=10
```

**Response (paginated):**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Complete project",
      "description": "Finish the Spring Boot API",
      "completed": false,
      "createdAt": "2026-02-08T10:30:00",
      "updatedAt": "2026-02-08T10:30:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

**Query Parameters:**
- `completed` (Boolean): Filter by completion status
- `page` (Integer): Page number (0-indexed)
- `size` (Integer): Items per page
- `sortBy` (String): Field to sort by (default: "id")
- `direction` (String): Sort direction - ASC or DESC (default: "ASC")

### Get a specific task
```http
GET /api/tasks/{id}
```

**Success Response (200):**
```json
{
  "id": 1,
  "title": "Complete project",
  "description": "Finish the Spring Boot API",
  "completed": false,
  "createdAt": "2026-02-08T10:30:00",
  "updatedAt": "2026-02-08T10:30:00"
}
```

**Error Response (404):**
```json
{
  "timestamp": "2026-02-08T12:00:00",
  "status": 404,
  "message": "Task not found with id: 999"
}
```

### Create a new task
```http
POST /api/tasks
Content-Type: application/json

{
  "title": "New task",
  "description": "Task description"
}
```

**Validation Rules:**
- `title`: Required, 3-100 characters
- `description`: Optional, max 500 characters

**Validation Error (400):**
```json
{
  "timestamp": "2026-02-08T12:00:00",
  "status": 400,
  "errors": {
    "title": "Title must be between 3 and 100 characters"
  }
}
```

**Success Response (201):**
```json
{
  "id": 2,
  "title": "New task",
  "description": "Task description",
  "completed": false,
  "createdAt": "2026-02-08T12:00:00",
  "updatedAt": "2026-02-08T12:00:00"
}
```

### Update a task
```http
PUT /api/tasks/{id}
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description"
}
```

### Toggle task completion
```http
PATCH /api/tasks/{id}/toggle
```

Switches the `completed` status between `true` and `false`.

### Delete a task
```http
DELETE /api/tasks/{id}
```

Returns `204 No Content` on success.

## ✅ Input Validation

All endpoints validate input data:

| Field | Rules |
|-------|-------|
| **title** | Required, 3-100 characters |
| **description** | Optional, max 500 characters |

Invalid requests return `400 Bad Request` with detailed error messages in the following format:

```json
{
  "timestamp": "2026-02-08T12:00:00",
  "status": 400,
  "errors": {
    "fieldName": "Error message"
  }
}
```

## 🧪 Running Tests

Execute the test suite:

```bash
mvn test
```

**Test Coverage:**
- Service layer: 11 unit tests
- Controller layer: 9 integration tests
- **Total: 20 tests**
- Test scenarios: Create, Read, Update, Delete, Toggle, Pagination, Filtering, Validation

**Sample test output:**
```
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
```

## 🔄 CI/CD Pipeline

The project includes a GitHub Actions workflow that:
- Runs on every push to any branch
- Compiles the project
- Executes all tests
- Generates test reports
- Uploads test artifacts

View workflow status in the **Actions** tab on GitHub.

## 📊 Database

The application uses H2 in-memory database. Access the H2 console at:

```
http://localhost:8080/h2-console
```

**Connection details:**
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: *(leave empty)*

## 🏗️ Project Structure

```
src/main/java/
├── controller/          # REST endpoints
├── service/             # Business logic layer
├── repository/          # Data access layer
├── model/               # Entity classes
├── dto/                 # Data Transfer Objects
├── exception/           # Custom exceptions and handlers
└── config/              # Configuration classes (Swagger, etc.)

src/test/java/
├── service/             # Service unit tests
└── controller/          # Controller integration tests
```

## 🎯 Architecture Highlights

### DTOs (Data Transfer Objects)
Separate DTOs for requests and responses:
- **TaskCreateDTO**: For creating/updating tasks (input validation)
- **TaskResponseDTO**: For API responses (with timestamps)
- **PageResponseDTO**: Generic wrapper for paginated responses

Benefits:
- Hide internal entity structure
- Control exactly what data is exposed
- Enable different validation rules for different operations
- Clean separation of concerns

### Pagination & Sorting
Uses Spring Data's `Pageable` interface:
- Efficient database queries with `LIMIT` and `OFFSET`
- Configurable page size and sorting
- Metadata in responses (totalPages, totalElements, etc.)
- Works seamlessly with filtering

### Global Exception Handling
Centralized error handling with `@ControllerAdvice`:
- Consistent error response format across all endpoints
- Meaningful HTTP status codes (400, 404, 500)
- Detailed validation error messages
- Separate handling for different exception types

### Transactional Service Layer
All service methods are transactional:
- `@Transactional(readOnly = true)` for queries (optimization)
- `@Transactional` for modifications (data consistency)
- Automatic rollback on exceptions
- ACID guarantees

## 📝 Example Usage

### Creating a task with cURL:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Complete REST API tutorial"
  }'
```

### Getting paginated tasks:
```bash
curl "http://localhost:8080/api/tasks?page=0&size=5&sortBy=createdAt&direction=DESC"
```

### Filtering completed tasks:
```bash
curl "http://localhost:8080/api/tasks?completed=true"
```

### Toggling task completion:
```bash
curl -X PATCH http://localhost:8080/api/tasks/1/toggle
```

## 🔜 Future Improvements

- [ ] Integrate PostgreSQL for production
- [ ] Implement Spring Security with JWT authentication
- [ ] Add role-based access control (RBAC)
- [ ] Implement task categories/tags with Many-to-Many relationship
- [ ] Add search functionality with full-text search
- [ ] Implement soft delete for tasks
- [ ] Add task priority levels
- [ ] Implement task due dates with reminders
- [ ] Add Docker support with docker-compose
- [ ] Deploy to cloud platform (AWS/Heroku)
- [ ] Add monitoring with Actuator
- [ ] Implement caching with Redis

## 👨‍💻 Author

**Rodrigo Bellanti**
- GitHub: [@RodrigoBellanti](https://github.com/RodrigoBellanti)
- LinkedIn: [Rodrigo Bellanti](https://www.linkedin.com/in/rodrigo-bellanti/)

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

**Note:** This is a learning project demonstrating Spring Boot best practices including RESTful API design, DTOs, validation, error handling, pagination, API documentation, and comprehensive testing.