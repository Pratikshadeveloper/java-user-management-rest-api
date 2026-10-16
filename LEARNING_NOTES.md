# Module 5: Java Web & REST API Development - Comprehensive Learning Notes

> **Document Purpose:** Complete study and revision notes for *Module 5: Java Web & REST API Development*. Designed for direct import into Google Docs, academic evaluation, and technical portfolio presentation.

---

## 1. What is REST (Representational State Transfer)?

**REST** is an architectural style designed by Roy Fielding in his 2000 doctoral dissertation for building distributed networked systems. In modern web development, REST APIs provide a standardized, lightweight protocol for communication between client applications (web browsers, mobile apps, Postman) and backend servers over HTTP.

### The 6 Guiding Architectural Constraints of REST:
1. **Client-Server Architecture:** Separates user interface concerns (client) from data storage and business logic concerns (server). This enhances portability, scalability, and independent deployment cycles.
2. **Statelessness:** Every request sent from client to server must contain all necessary information for the server to understand and process the request. No client context is stored on the server between requests (session state is maintained purely by the client or via tokens like JWT).
3. **Cacheability:** Responses must explicitly indicate whether they can be cached by the client or intermediary proxies to improve network efficiency and eliminate redundant server processing.
4. **Uniform Interface:** The cornerstone of REST design:
   - *Resource Identification:* Resources are identified by stable URIs (e.g., `/api/users`, `/api/users/1`).
   - *Manipulation of Resources Through Representations:* Clients modify resources by exchanging representations (e.g., JSON, XML).
   - *Self-descriptive Messages:* Each message contains sufficient metadata (Content-Type, status code) to explain how to parse it.
   - *HATEOAS (Hypermedia As The Engine Of Application State):* Clients discover actions dynamically through embedded links.
5. **Layered System:** A client cannot tell whether it is connected directly to the end server or to an intermediary (load balancer, API gateway, reverse proxy, CDN).
6. **Code on Demand (Optional):** Servers can temporarily extend client functionality by transferring executable code (e.g., JavaScript).

---

## 2. HTTP Methods & Response Status Codes

REST uses standard HTTP request methods (verbs) to perform CRUD operations on resources:

| HTTP Verb | CRUD Operation | Idempotent | Safe | Typical Response Code | Description |
|-----------|----------------|------------|------|-----------------------|-------------|
| **POST**  | Create | No | No | `201 Created` / `400 Bad Request` | Creates a new resource on the server. Request body contains resource data. |
| **GET**   | Read | Yes | Yes | `200 OK` / `404 Not Found` | Retrieves resource representation. Must never modify server state. |
| **PUT**   | Update (Full) | Yes | No | `200 OK` / `404 Not Found` | Replaces the entire target resource with payload. |
| **PATCH** | Update (Partial) | No | No | `200 OK` / `404 Not Found` | Applies partial modifications to a resource. |
| **DELETE**| Delete | Yes | No | `200 OK` / `204 No Content` / `404 Not Found` | Removes the target resource. |

### Key HTTP Status Code Categories:
- **`2xx Success`**:
  - `200 OK`: Request succeeded. Returned on GET, PUT, and DELETE.
  - `201 Created`: New resource was successfully created. Returned on POST.
  - `204 No Content`: Request succeeded with no content returned in the response body.
- **`4xx Client Error`**:
  - `400 Bad Request`: Server cannot process request due to malformed syntax, invalid JSON, or bean validation failure.
  - `401 Unauthorized`: Request lacks valid authentication credentials.
  - `403 Forbidden`: Authenticated user lacks permission to access the resource.
  - `404 Not Found`: Requested resource URI does not exist.
  - `409 Conflict`: Request conflicts with current server state (e.g., duplicate email address).
- **`5xx Server Error`**:
  - `500 Internal Server Error`: Unhandled server exception or unexpected infrastructure crash.

---

## 3. Spring Boot Core Annotations Explained

Spring Boot leverages stereotyping annotations to configure Spring IoC container components, dependency injection, and HTTP routing:

### 3.1 Web & Routing Annotations
- **`@RestController`**: Specialization of `@Controller` that marks the class as a web controller where every handler method automatically writes response data directly into the HTTP response body (implicitly applies `@ResponseBody` to all methods). Returns data formatted as JSON via Jackson.
- **`@RequestMapping("/api/users")`**: Specifies the base URI mapping for all endpoints in the controller.
- **`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`**: Shortcut annotations mapped to specific HTTP methods (`@RequestMapping(method = RequestMethod.GET)`, etc.).
- **`@RequestBody`**: Maps incoming HTTP request JSON body directly to a Java DTO object using Jackson's `ObjectMapper`.
- **`@PathVariable`**: Extracts dynamic template URI parameters (e.g., `@PathVariable Long id` from `/api/users/{id}`).
- **`@RequestParam`**: Extracts query parameters (e.g., `/api/users?page=1&size=10`).
- **`@Valid`**: Triggers JSR-380 Bean Validation on incoming DTOs prior to controller method execution.

### 3.2 Service & Dependency Injection Annotations
- **`@Service`**: Stereotype annotation denoting a class in the Service/Business Logic Layer. Registered as a Spring-managed singleton bean in the ApplicationContext.
- **`@Transactional`**: Manages database transaction boundaries automatically. Wraps method execution in begin, commit, and rollback on runtime exceptions.

### 3.3 Persistence & Entity Annotations
- **`@Repository`**: Stereotype annotation denoting data access layer components. Enables automatic translation of database-specific exceptions into Spring's unified `DataAccessException` hierarchy.
- **`@Entity`**: Marks a Java class as an ORM managed persistence entity mapped to a database table.
- **`@Table(name = "users")`**: Configures database table properties (name, schema, unique constraints, indexes).
- **`@Id`**: Designates the primary key field.
- **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**: Configures auto-incrementing primary key generation delegated to the underlying database engine.
- **`@Column(nullable = false, unique = true)`**: Specifies column-level schema definitions.
- **`@PrePersist` / `@PreUpdate`**: Entity lifecycle callbacks that automatically trigger before persistence (e.g., stamping `createdAt` timestamp).

---

## 4. MVC Architecture in Spring Boot

Spring Boot follows the **Model-View-Controller (MVC)** layered pattern, adapted for REST APIs:

```
[ HTTP Client (Postman / Browser) ]
             |
             | (1) HTTP Request (JSON Body, URI, Headers)
             v
   [ Controller Layer ] (UserController)
        - Receives HTTP request
        - Validates input DTOs with @Valid
        - Delegates to Service Layer
        - Constructs ResponseEntity (HTTP Status + Response Body)
             |
             | (2) Passes UserRequestDTO
             v
   [ Service Layer ] (UserServiceImpl)
        - Implements Business Rules (e.g. Email Uniqueness)
        - DTO <-> Entity Conversion
        - Transaction boundaries (@Transactional)
             |
             | (3) Interacts with Entity
             v
   [ Repository Layer ] (UserRepository)
        - Spring Data JPA abstraction
        - Translates queries into SQL/HQL
             |
             | (4) SQL Execution
             v
   [ Database Layer ] (H2 In-Memory / MySQL)
```

### Why Separate DTOs (Data Transfer Objects) from Entities?
1. **Security (Over-Posting Prevention):** Prevents malicious clients from tampering with internal properties (e.g., `id`, `role`, `createdAt`, `passwordHash`).
2. **Encapsulation & Decoupling:** Changes to internal database schemas do not break external public API contracts.
3. **Data Formatting & Projection:** Allows formatting responses specifically for client needs without polluting database entities.

---

## 5. Spring Data JPA & Hibernate

- **JPA (Jakarta Persistence API):** A Java specification describing how relational data should be managed and mapped to Java objects (ORM - Object Relational Mapping).
- **Hibernate:** The industry-standard implementation of the JPA specification. Generates SQL queries, manages session caching, and translates result sets to entities.
- **Spring Data JPA:** A high-level abstraction built on top of JPA that virtually eliminates boilerplate data access code.

### The Power of `JpaRepository`:
By extending `JpaRepository<User, Long>`, Spring Data dynamically generates proxy implementations at runtime with built-in methods:
- `save(S entity)`: Inserts or updates an entity.
- `findById(ID id)`: Retrieves an entity wrapped in an `Optional<T>`.
- `findAll()`: Returns all records in the table.
- `delete(T entity)` / `deleteById(ID id)`: Removes records.
- **Derived Query Methods:** Spring Data parses method names automatically into SQL:
  - `findByEmail(String email)` -> `SELECT * FROM users WHERE email = ?`
  - `existsByEmail(String email)` -> `SELECT count(*) > 0 FROM users WHERE email = ?`
  - `existsByEmailAndIdNot(String email, Long id)` -> `SELECT count(*) > 0 FROM users WHERE email = ? AND id <> ?`

---

## 6. Hibernate Validation (JSR 380)

Validation ensures that incoming requests adhere to strict business rules before reaching business logic:
- **`@NotBlank`**: Field must not be null and trimmed length must be greater than zero (for Strings).
- **`@Email`**: Validates standard email address syntax (`user@example.com`).
- **`@NotNull`**: Field must not be null (used for numeric types and objects).
- **`@Min(value = 1)`**: Number must be greater than or equal to 1.
- **`@Size(min = 2, max = 100)`**: Enforces string length boundaries.

When validation fails on a controller parameter annotated with `@Valid`, Spring throws a `MethodArgumentNotValidException`, which is caught by our centralized exception handler.

---

## 7. Custom Exception Handling Architecture

Robust REST APIs never expose raw stack traces or default 500 error pages to clients. Instead, they provide predictable, clean JSON payloads.

### Architectural Components:
1. **Custom Exceptions (`UserNotFoundException`, `UserAlreadyExistsException`):**
   - Extend `RuntimeException` to enable unchecked transaction rollbacks.
2. **Unified Error DTO (`ErrorResponse`):**
   - Attributes: `timestamp`, `status`, `error`, `message`, `path`, and `validationErrors`.
3. **Global Exception Handler (`GlobalExceptionHandler`):**
   - Annotated with `@RestControllerAdvice`.
   - Uses `@ExceptionHandler(ExceptionType.class)` to intercept specific exceptions across all controllers.
   - Converts exceptions into appropriate HTTP status codes:
     - `UserNotFoundException` -> `404 Not Found`
     - `UserAlreadyExistsException` -> `400 Bad Request` / `409 Conflict`
     - `MethodArgumentNotValidException` -> `400 Bad Request` with field-level map
     - `Exception` -> `500 Internal Server Error`

---

## 8. Summary Checklist for Evaluation

- [x] Full RESTful CRUD capabilities implemented across 5 endpoints
- [x] Layered MVC separation (Controller -> Service -> Repository -> Entity)
- [x] Strong encapsulation using separate Request/Response DTOs
- [x] JSR-380 input validation with descriptive custom messages
- [x] Clean centralized error handling returning RFC-compliant JSON responses
- [x] Zero-setup in-memory H2 database with H2 Console access
- [x] Production-ready MySQL profile provided
