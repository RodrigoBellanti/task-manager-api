![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Tests](https://img.shields.io/badge/tests-11%20passing-success)

## 🚀 Features

- RESTful API design with proper HTTP methods and status codes
- DTO pattern for clean separation between API and domain models
- Comprehensive input validation with custom error messages
- Global exception handling with meaningful error responses
- H2 in-memory database for quick setup and testing
- JPA/Hibernate for data persistence
- Transactional service layer
- Unit tests with Mockito (90%+ coverage)
- Automatic timestamps for created/updated records

## 🛠️ Technologies

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Validation**
- **H2 Database**
- **Lombok**
- **JUnit 5**
- **Mockito**
- **Maven**

## 📋 Prerequisites

- JDK 17 or higher
- Maven 3.6+
- (Optional) Insomnia, Postman, or similar tool for testing

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

## 🔌 API Endpoints

### Get all tasks
```http
GET /api/tasks
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Complete project",
    "description": "Finish the Spring Boot API",
    "completed": false,
    "createdAt": "2026-02-08T10:30:00",
    "updatedAt": "2026-02-08T10:30:00"
  }
]
```

### Get tasks by status
```http
GET /api/tasks/status?completed=false
```

### Get a specific task
```http
GET /api/tasks/{id}
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

### Delete a task
```http
DELETE /api/tasks/{id}
```

## ✅ Input Validation

All endpoints validate input data:

| Field | Rules |
|-------|-------|
| **title** | Required, 3-100 characters |
| **description** | Optional, max 500 characters |

Invalid requests return `400 Bad Request` with detailed error messages.

## 🧪 Running Tests

Execute the test suite:

```bash
mvn test
```

**Test Coverage:**
- Service layer: 11 unit tests
- Test scenarios: Create, Read, Update, Delete, Toggle, Filter
- Edge cases: Not found errors, validation errors

**Sample test output:**
```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
```

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
└── exception/           # Custom exceptions and handlers

src/test/java/
└── service/             # Unit tests
```

## 🎯 Architecture Highlights

### DTOs (Data Transfer Objects)
Separate DTOs for requests and responses to:
- Hide internal entity structure
- Control exactly what data is exposed
- Enable different validation rules for different operations

### Global Exception Handling
Centralized error handling with `@ControllerAdvice`:
- Consistent error response format
- Meaningful HTTP status codes
- Detailed validation error messages

### Transactional Service Layer
All service methods are transactional:
- `@Transactional(readOnly = true)` for queries
- `@Transactional` for modifications
- Ensures data consistency

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

### Filtering completed tasks:
```bash
curl http://localhost:8080/api/tasks/status?completed=true
```

### Toggling task completion:
```bash
curl -X PATCH http://localhost:8080/api/tasks/1/toggle
```

## 🔜 Future Improvements

- [ ] Add pagination and sorting
- [ ] Integrate PostgreSQL for production
- [ ] Implement Spring Security with JWT
- [ ] Add integration tests
- [ ] Implement API documentation with Swagger/OpenAPI
- [ ] Add Docker support
- [ ] Implement CI/CD pipeline
- [ ] Add task categories/tags
- [ ] Implement search functionality

## 👨‍💻 Author

**Rodrigo Bellanti**
- GitHub: [@RodrigoBellanti](https://github.com/RodrigoBellanti)
- LinkedIn: [Rodrigo Bellanti](https://www.linkedin.com/in/rodrigo-bellanti/)
- 
## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

**Note:** This is a learning project demonstrating Spring Boot best practices including DTOs, validation, error handling, and comprehensive testing.