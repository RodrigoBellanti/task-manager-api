# Task Manager REST API

A RESTful API for task management built with Spring Boot and Java 17. This project demonstrates CRUD operations, JPA/Hibernate integration, and REST API best practices.

## 🚀 Features

- Create, read, update, and delete tasks
- RESTful API design with proper HTTP methods
- H2 in-memory database for quick setup
- JPA/Hibernate for data persistence
- Clean architecture with service layer separation

## 🛠️ Technologies

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
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

### Get a specific task
```http
GET /api/tasks/{id}
```

### Create a new task
```http
POST /api/tasks
Content-Type: application/json

{
  "title": "Task title",
  "description": "Task description",
  "completed": false
}
```

### Update a task
```http
PUT /api/tasks/{id}
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "completed": true
}
```

### Delete a task
```http
DELETE /api/tasks/{id}
```

## 📊 Database

The application uses H2 in-memory database. You can access the H2 console at:

```
http://localhost:8080/h2-console
```

Connection details:
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: *(leave empty)*

## 📝 Example Usage

### Creating a task:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Complete REST API tutorial",
    "completed": false
  }'
```

### Response:
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Complete REST API tutorial",
  "completed": false
}
```

## 🏗️ Project Structure

```
src/main/java/
├── controller/     # REST endpoints
├── model/          # Entity classes
├── repository/     # Data access layer
└── service/        # Business logic
```

## 🔜 Future Improvements

- [ ] Add input validation
- [ ] Implement exception handling
- [ ] Add pagination for task listing
- [ ] Integrate PostgreSQL for production
- [ ] Add unit and integration tests
- [ ] Implement authentication/authorization
- [ ] Add API documentation with Swagger/OpenAPI

## 👨‍💻 Author

**[Tu Nombre]**
- GitHub: [@RodrigoBellanti](https://github.com/RodrigoBellanti)
- LinkedIn: [Rodrigo Bellanti](https://www.linkedin.com/in/rodrigo-bellanti/)

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
