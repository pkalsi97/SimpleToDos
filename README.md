
---

# SimpleToDos

SimpleToDos is a Java Spring Boot application that provides a platform for managing task lists and tasks. It features JWT-based authentication, RESTful APIs for task management, and user registration functionality.

## Features

- **User Authentication**: Register new users and authenticate via JWT.
- **Task Management**: Create, update, fetch, and delete tasks and task lists.
- **Security**: JWT token validation for secure API access.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java JDK 11 or later
- Maven
- MySQL

### Installing

A step by step series of examples that tell you how to get a development environment running.

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/SimpleToDos.git
   ```
2. **Configure MySQL Database**
   - Create a new database named `simpletodos`.
   - Update `application.properties` with your MySQL user and password.

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   The server will start on `http://localhost:8080`.

---

## Configuring Application Properties

Before running the application, you need to set up the `application.properties` file with the necessary environment variables. This file is located in the `src/main/resources` directory of the project.

### Environment Variables

The following environment variables need to be set in the `application.properties` file:

- `DB_HOST`: Hostname of your MySQL server (e.g., `localhost`).
- `DB_PORT`: Port number for your MySQL server (e.g., `3306`).
- `DB_NAME`: Name of your MySQL database (e.g., `simpletodos`).
- `DB_USERNAME`: Username for your MySQL database.
- `DB_PASSWORD`: Password for your MySQL database.
- `TOKEN_SECRET_KEY`: A secret key used for signing JWT tokens.

Here's a sample configuration:

```properties
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?ssl-mode=REQUIRED
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
token.signing.key=${TOKEN_SECRET_KEY}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML
spring.thymeleaf.servlet.content-type=text/html
```

### Configuring the Environment Variables

You can either set these variables directly in the `application.properties` file or use an external configuration method like environment variables or a `.env` file. If using an external method, ensure these variables are accessible by the application at runtime.

---

## Usage

- **Register a User**: Navigate to `http://localhost:8080/register` to create a new user.
- **Generate JWT Token**: Use the `/authenticate` endpoint to obtain a JWT token.
- **Accessing Task APIs**: Use the provided JWT token to authenticate and access task-related APIs.

Sure, I'll provide curl commands for interacting with your RESTful APIs, covering authentication and all task-related operations. Ensure your application is running and accessible for these commands to work.

### 1. User Authentication

To authenticate a user and receive a JWT token:

```bash
curl -X POST http://localhost:8080/authenticate \
     -H "Content-Type: application/json" \
     -d '{"username":"your_username","password":"your_password"}'
```

### 2. Register a User

To register a new user:

```bash
curl -X POST http://localhost:8080/register \
     -H "Content-Type: application/json" \
     -d '{"username":"new_username", "email":"new_email", "password":"new_password", "confirmPassword":"new_password"}'
```

### 3. Create a Task List

To create a new task list:

```bash
curl -X POST http://localhost:8080/api/v1/tasklists \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer your_jwt_token" \
     -d '{"name":"Task List Name", "description":"Task List Description"}'
```

### 4. Update a Task List

To update an existing task list:

```bash
curl -X PUT http://localhost:8080/api/v1/tasklists \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer your_jwt_token" \
     -d '{"uuid":"task_list_uuid", "description":"New Task List Description"}'
```

### 5. Get a Task List

To retrieve a specific task list by its UUID:

```bash
curl -X GET http://localhost:8080/api/v1/tasklists/task_list_uuid \
     -H "Authorization: Bearer your_jwt_token"
```

### 6. Delete a Task List

To delete a specific task list by its UUID:

```bash
curl -X DELETE http://localhost:8080/api/v1/tasklists/task_list_uuid \
     -H "Authorization: Bearer your_jwt_token"
```

### 7. Create a Task

To create a new task:

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer your_jwt_token" \
     -d '{"name":"Task Name", "description":"Task Description", "tasklist_uuid":"task_list_uuid"}'
```

### 8. Update a Task

To update an existing task:

```bash
curl -X PUT http://localhost:8080/api/v1/tasks \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer your_jwt_token" \
     -d '{"uuid":"task_uuid", "description":"New Task Description", "status":"Task Status"}'
```

### 9. Get a Task

To retrieve a specific task by its UUID:

```bash
curl -X GET http://localhost:8080/api/v1/tasks/task_uuid \
     -H "Authorization: Bearer your_jwt_token"
```

### 10. Delete a Task

To delete a specific task by its UUID:

```bash
curl -X DELETE http://localhost:8080/api/v1/tasks/task_uuid \
     -H "Authorization: Bearer your_jwt_token"
```

### 11. Get Tasks for a Task List

To retrieve all tasks associated with a specific task list:

```bash
curl -X GET http://localhost:8080/api/v1/tasks/tasklist/task_list_uuid \
     -H "Authorization: Bearer your_jwt_token"
```

Replace placeholders like `your_username`, `your_password`, `your_jwt_token`, `task_list_uuid`, and `task_uuid` with actual values. The above commands assume that your application is running on `localhost:8080`. Adjust the host and port as per your deployment.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Maven](https://maven.apache.org/) - Dependency Management
- [MySQL](https://www.mysql.com/) - Database

---
