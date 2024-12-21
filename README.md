# TaskManager Application

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Prerequisites](#prerequisites)
4. [Running the Application](#running-the-application)
   - [Using Docker](#using-docker)
   - [Using Gradle](#using-gradle)
5. [Accessing the API](#accessing-the-api)
   - [Swagger UI](#swagger-ui)
   - [Postman Collection](#postman-collection)
6. [User Activation and Permissions Setup](#user-activation-and-permissions-setup)
7. [API Documentation](#api-documentation)
8. [Default Admin Credentials](#default-admin-credentials)
9. [Application Tests](#application-tests)

---

<div id='project-overview'/>
## Project Overview 

The **TaskManager** is a RESTful API application built with Java, Spring Boot, and Gradle. It provides functionalities for:
- **User Management**: Register, login, role management, and permissions.
- **Task Management**: CRUD operations on tasks.
- **Admin Features**: User activation, role assignment, and task operations.

---

<div id='architecture'/>
## Architecture

The application follows the **MVC (Model-View-Controller)** design pattern:
- **Controller Layer**: Handles HTTP requests and responses.
- **Service Layer**: Contains business logic.
- **Repository Layer**: Interacts with the database using JPA.
- **Security Layer**: Implements JWT-based authentication.

### Key Technologies
- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Security**
- **H2 Database** (in-memory)
- **Docker**
- **Gradle**
- **JWT Authentication**
- **Swagger (springdoc-openapi)** for API documentation
- **Testcontainers** for integration tests with real database environments

---

<div id='prerequisites'/>
## Prerequisites

Before running the application, ensure you have the following installed:
1. **Java 17** (Verify with `java --version`)
2. **Gradle** (Verify with `gradle --version`)
3. **Docker** (Verify with `docker --version`)
4. **Postman** (optional, for API testing)

---

<div id='running-the-application'/>
## Running the Application

### 1. Using Docker

1. **Build and Run with Docker**:
    ```bash
    ./build-and-run.sh
    ```
   This script will:
   - Build the JAR file using Gradle.
   - Build a Docker image.
   - Start the application in a Docker container.

2. **Verify the Docker container**:
    ```bash
    docker ps
    ```

3. **Access the application**:
   - **Swagger UI**: [http://localhost:8081/taskmanager/swagger-ui/index.html](http://localhost:8081/taskmanager/swagger-ui/index.html)

---

### 2. Using Gradle (without Docker)

1. **Run the Application Locally**:
    ```bash
    ./gradlew clean build
    java -jar build/libs/taskmanager-0.0.1-SNAPSHOT.jar
    ```

2. **Access the application**:
   - **Swagger UI**:  [http://localhost:8081/taskmanager/swagger-ui/index.html](http://localhost:8081/taskmanager/swagger-ui/index.html)

---

<div id='accessing-the-api'/>
## Accessing the API

### Swagger UI

The Swagger UI provides an interactive way to test the API:
- Open your browser and go to: [http://localhost:8081/taskmanager/swagger-ui/index.html](http://localhost:8081/taskmanager/swagger-ui/index.html)

### Postman Collection

The Postman collection for testing is located in: `/postman/TaskManager_API_Collection.json`

1. Import the collection into Postman.
2. Set the environment variable `baseUrl` to: `http://localhost:8081/taskmanager`

3. Follow the requests for **User Operations** and **Admin Operations**.

---

<div id='user-activation-and-permissions-setup'/>
## User Activation and Permissions Setup

### Step-by-Step Guide to Enable a User to Perform Operations

1. **Register a New User**
    - Endpoint: `POST /api/v1/auth/register`
    - Use the Swagger UI or Postman to send a request with the following payload:
      ```json
      {
          "username": "testuser",
          "email": "testuser@example.com",
          "password": "password123"
      }
      ```
    - The user will be created in an **INACTIVE** state by default.

2. **Admin Login**
    - Endpoint: `POST /api/v1/auth/login`
    - Use the Swagger UI or Postman to send a request with the following payload:
      ```json
      {
          "username": "admin",
          "password": "admin123"
      }
      ```
    - Copy the `adminToken` from the response. This token will be used for administrative operations.

3. **Activate the User**
    - Endpoint: `PATCH /api/v1/users/{userId}/status`
    - Use the Swagger UI or Postman with the `adminToken` to activate the user:
      - Replace `{userId}` with the ID of the user to activate.
      - Example URL: `http://localhost:8081/api/v1/users/{userId}/status?status=ACTIVE`
    - The `adminToken` should be included in the request headers:
      ```json
      {
          "Authorization": "Bearer {adminToken}"
      }
      ```

4. **Assign Permissions to the User**
    - Endpoint: `PATCH /api/v1/users/{userId}/permissions`
    - Use the Swagger UI or Postman with the `adminToken` to assign permissions to the user:
      - Replace `{userId}` with the ID of the user to assign permissions.
      - Example body:
        ```json
        ["READ_TASKS", "CREATE_TASKS", "DELETE_TASKS"]
        ```

5. **User Login**
    - Endpoint: `POST /api/v1/auth/login`
    - Use the Swagger UI or Postman to send a request with the registered user’s credentials:
      ```json
      {
          "username": "testuser",
          "password": "password123"
      }
      ```
    - Copy the `userToken` from the response. This token will be used to perform user-specific operations.

6. **Perform User Operations**
    - The user can now perform tasks like creating, reading, updating, or deleting tasks using their `userToken`.

---

<div id='api-documentation'/>
## API Documentation

### Authentication
| Method | Endpoint             | Description               |
|--------|----------------------|---------------------------|
| POST   | `/api/v1/auth/register` | Register a new user      |
| POST   | `/api/v1/auth/login`    | Login to get JWT token   |

### Tasks
| Method | Endpoint                 | Description                 |
|--------|--------------------------|-----------------------------|
| GET    | `/api/v1/tasks`          | Get all tasks               |
| POST   | `/api/v1/tasks`          | Create a new task           |
| PUT    | `/api/v1/tasks/{id}`     | Update a specific task      |
| PATCH  | `/api/v1/tasks/{id}/status` | Update task status         |
| DELETE | `/api/v1/tasks/{id}`     | Delete a specific task      |

### Admin Operations
| Method | Endpoint                     | Description                  |
|--------|------------------------------|------------------------------|
| PATCH  | `/api/v1/users/{id}/status`  | Activate/Deactivate a user   |
| PATCH  | `/api/v1/users/{id}/role`    | Update user role             |
| PATCH  | `/api/v1/users/{id}/permissions` | Assign permissions to a user |

---

<div id='default-admin-credentials'/>
## Default Admin Credentials

- **Username**: `admin`
- **Password**: `admin123`

---

<div id='application-tests'/>
## Application Tests

The application includes comprehensive tests using **Testcontainers** to ensure reliability and correctness:
- **Integration Tests**: Use Testcontainers to spin up a real PostgreSQL database for testing.
- **Security Tests**: Validate authentication and authorization flows using `spring-security-test`.
- **Run Tests**:
    ```bash
    ./gradlew test
    ```
  Test results will be available under `build/reports/tests/test/index.html`.

---

Happy Task Managing! 🚀
