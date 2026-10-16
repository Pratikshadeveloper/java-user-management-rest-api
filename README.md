# User Management REST API - Module 5

A production-ready Spring Boot 3 RESTful web service for managing users and performing complete CRUD operations, adhering to clean architecture, enterprise error handling, and robust data validation.

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://spring.io/projects/spring-data-jpa)
[![H2 Database](https://img.shields.io/badge/H2-In--Memory-007ACC?style=for-the-badge)](https://www.h2database.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

---

## 📋 Table of Contents
- [Project Overview](#-project-overview)
- [Architecture & Design](#-architecture--design)
- [Tech Stack](#-tech-stack)
- [Getting Started & Running](#-getting-started--running)
- [API Endpoints Reference](#-api-endpoints-reference)
- [Example Payloads & cURL Tests](#-example-payloads--curl-tests)
- [H2 Web Console](#-h2-web-console)
- [Switching to MySQL](#-switching-to-mysql)
- [Testing with Postman](#-testing-with-postman)
- [Learning Resources](#-learning-resources)

---

## 🌟 Project Overview

This project was built for **Module 5: Java Web & REST API Development**. It implements a complete, validated User Management API with:
- **5 RESTful CRUD Endpoints** (Create, Read All, Read by ID, Update, Delete)
- **Model-View-Controller (MVC)** layered enterprise pattern
- **DTO Layering:** Segregation between request payloads (`UserRequestDTO`) and API output (`UserResponseDTO`)
- **Hibernate Bean Validation:** Pre-flight validation enforcing valid emails, non-blank strings, and age limits
- **Global Error Handling:** Unified, centralized `@RestControllerAdvice` returning structured JSON error payloads
- **Dual Persistence:** Zero-setup H2 In-Memory database by default, with ready-to-uncomment MySQL support

---

## 🏗️ Architecture & Design

```
src/main/java/com/usermanagement/api/
├── UserManagementApiApplication.java   # Spring Boot entry point & demo data seeder
├── controller/
│   └── UserController.java             # REST controller exposing /api/users
├── service/
│   ├── UserService.java                # Business service interface
│   └── impl/
│       └── UserServiceImpl.java         # Business logic & transactional operations
├── repository/
│   └── UserRepository.java             # Spring Data JPA repository
├── entity/
│   └── User.java                       # JPA Entity mapped to 'users' table
├── dto/
│   ├── UserRequestDTO.java             # Input validation DTO
│   ├── UserResponseDTO.java            # Clean output response DTO
│   └── ErrorResponse.java              # Unified JSON error format
└── exception/
    ├── UserNotFoundException.java       # Custom 404 exception
    ├── UserAlreadyExistsException.java  # Custom duplicate error exception
    └── GlobalExceptionHandler.java      # Centralized @RestControllerAdvice
```

---

## 💻 Tech Stack

- **Language:** Java 17+ (LTS)
- **Framework:** Spring Boot 3.2.5
- **Modules:**
  - `spring-boot-starter-web` (REST endpoints, Jackson JSON processing)
  - `spring-boot-starter-data-jpa` (Hibernate ORM persistence)
  - `spring-boot-starter-validation` (Hibernate Validator / JSR-380)
  - `spring-boot-starter-test` (JUnit 5, Mockito)
- **Database:** H2 In-Memory Database (runtime) / MySQL Connector/J
- **Build Tool:** Apache Maven 3.9+ / Maven Wrapper (`mvnw`)
- **Utilities:** Project Lombok

---

## 🚀 Getting Started & Running

### Prerequisites
- Java JDK 17 or higher (`java -version`)
- Git

### 1. Clone the repository
```bash
git clone <your-github-repo-url>
cd java-rest-api-project
```

### 2. Run with Maven Wrapper (Zero Installation Needed)
On Windows:
```cmd
.\mvnw.cmd spring-boot:run
```
On Linux / macOS:
```bash
./mvnw spring-boot:run
```

Or using an installed Maven:
```bash
mvn spring-boot:run
```

Once started, the application will be live at:
👉 **`http://localhost:8081/api/users`**

> **Note on Port Configuration:**
> The default port is configured to `8081` in [`application.properties`](src/main/resources/application.properties) because Oracle XE (TNSLSNR) commonly occupies port `8080` on local Windows workstations. You can switch to port `8080` anytime by editing `server.port=8080` or launching with `--server.port=8080`.

---

## 📡 API Endpoints Reference

Base URL: `http://localhost:8081/api/users`

| HTTP Method | Endpoint | Description | Status Code |
|-------------|----------|-------------|-------------|
| **POST** | `/api/users` | Create a new user | `201 Created` / `400 Bad Request` |
| **GET** | `/api/users` | Retrieve all registered users | `200 OK` |
| **GET** | `/api/users/{id}` | Retrieve user by ID | `200 OK` / `404 Not Found` |
| **PUT** | `/api/users/{id}` | Update existing user details | `200 OK` / `404 Not Found` / `400 Bad Request` |
| **DELETE** | `/api/users/{id}` | Delete user by ID | `200 OK` / `404 Not Found` |

---

## 🧪 Example Payloads & cURL Tests

### 1. Create User (POST)
**Request:**
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"age\": 29}"
```
**Response (`201 Created`):**
```json
{
  "id": 3,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "age": 29,
  "createdAt": "2026-10-16T21:15:45"
}
```

---

### 2. Get All Users (GET)
**Request:**
```bash
curl -X GET http://localhost:8081/api/users
```
**Response (`200 OK`):**
```json
[
  {
    "id": 1,
    "name": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "age": 28,
    "createdAt": "2026-10-16T21:14:01"
  },
  {
    "id": 2,
    "name": "Bob Smith",
    "email": "bob.smith@example.com",
    "age": 34,
    "createdAt": "2026-10-16T21:14:01"
  }
]
```

---

### 3. Get User By ID (GET)
**Request:**
```bash
curl -X GET http://localhost:8081/api/users/1
```
**Response (`200 OK`):**
```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "age": 28,
  "createdAt": "2026-10-16T21:14:01"
}
```

---

### 4. Update User (PUT)
**Request:**
```bash
curl -X PUT http://localhost:8081/api/users/1 \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"Alice J. Williams\", \"email\": \"alice.williams@example.com\", \"age\": 29}"
```
**Response (`200 OK`):**
```json
{
  "id": 1,
  "name": "Alice J. Williams",
  "email": "alice.williams@example.com",
  "age": 29,
  "createdAt": "2026-10-16T21:14:01"
}
```

---

### 5. Delete User (DELETE)
**Request:**
```bash
curl -X DELETE http://localhost:8081/api/users/1
```
**Response (`200 OK`):**
```json
{
  "success": true,
  "message": "User deleted successfully",
  "deletedUserId": 1
}
```

---

### 6. Validation Error Example (`400 Bad Request`)
**Request with invalid email and age:**
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"\", \"email\": \"invalid-email\", \"age\": 0}"
```
**Response (`400 Bad Request`):**
```json
{
  "timestamp": "2026-10-16T21:17:15",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed for one or more fields",
  "path": "/api/users",
  "validationErrors": {
    "name": "Name cannot be blank",
    "email": "Please provide a valid email address",
    "age": "Age must be at least 1"
  }
}
```

---

### 7. User Not Found Example (`404 Not Found`)
**Request for non-existent ID:**
```bash
curl -X GET http://localhost:8081/api/users/999
```
**Response (`404 Not Found`):**
```json
{
  "timestamp": "2026-10-16T21:17:56",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999",
  "path": "/api/users/999"
}
```

---

## 🗄️ H2 Web Console

The H2 in-memory web console is enabled for real-time table inspection:
- **URL:** [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
- **JDBC URL:** `jdbc:h2:mem:userdb`
- **User Name:** `sa`
- **Password:** *(leave blank)*

---

## 🐬 Switching to MySQL

To switch from H2 to MySQL:
1. Ensure a MySQL instance is running on port `3306`.
2. Open [`src/main/resources/application.properties`](src/main/resources/application.properties).
3. Comment out the H2 section.
4. Uncomment the MySQL section:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/user_management_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   spring.jpa.hibernate.ddl-auto=update
   ```
5. Restart the application.

---

## 📮 Testing with Postman

A complete Postman Collection is provided in the root folder:
📁 [`postman_collection.json`](postman_collection.json)

### How to Import:
1. Open **Postman**.
2. Click **Import** (top left).
3. Drag and drop or select [`postman_collection.json`](postman_collection.json).
4. Execute the requests sequentially in the **User Management REST API - Module 5** collection.

---

## 📚 Learning Resources

- Detailed architectural notes, annotation guides, and evaluation notes are available in:
  📄 [`LEARNING_NOTES.md`](LEARNING_NOTES.md) (ready for Google Docs submission).
- Professional announcement post draft is available in:
  📄 [`LINKEDIN_POST.md`](LINKEDIN_POST.md).
