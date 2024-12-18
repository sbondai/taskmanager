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
6. [API Documentation](#api-documentation)
 

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

## Accessing the API

### Swagger UI

The Swagger UI provides an interactive way to test the API:
- Open your browser and go to: http://localhost:8081/taskmanager/swagger-ui/index.html

### Postman Collection

The Postman collection for testing is located in: /postman/TaskManager_API_Collection.json

1. Import the collection into Postman.
2. Set the environment variable `baseUrl` to: http://localhost:8081/taskmanager

3. Follow the requests for **User Operations** and **Admin Operations**.

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
Happy Task Managing 🚀