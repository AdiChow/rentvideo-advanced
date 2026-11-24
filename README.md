# RentVideo – Advanced Video Rental System

A RESTful video rental API built with Spring Boot, Spring Security, Spring Data JPA, and MySQL. It provides JWT-based authentication, role-based authorization, video catalogue management, and rental tracking.

## Tech Stack

- Java 21
- Spring Boot 4.2.0
- Spring Security
- JWT authentication (JJWT)
- Spring Data JPA / Hibernate
- MySQL 8.4
- Gradle
- Docker Compose
- BCrypt password hashing

## Features

- User registration and login.
- Stateless JWT-based authentication.
- BCrypt password hashing.
- Role-based access control (`CUSTOMER` and `ADMIN`).
- Public registration defaults to `CUSTOMER`.
- Admin account provisioning through environment variables.
- Browse available videos.
- Admin-only video creation, update, and deletion.
- Rent and return videos.
- Maximum of two active rentals per customer.
- Prevention of duplicate active rentals.
- Request validation and centralized exception handling.

## Prerequisites

- Java 21
- Docker Desktop / Docker Engine
- Git

## Getting Started

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd rentvideo_advanced
```

### 2. Configure environment variables

Create your local environment file:

```bash
cp .env.example .env
```

Update `.env` with your own credentials:

```properties
MYSQL_PASSWORD=your_mysql_password
MYSQL_ROOT_PASSWORD=your_root_password
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=your_admin_password
JWT_SECRET=your_base64_encoded_secret
JWT_EXPIRATION=86400000
```

Do not commit your `.env` file or expose your JWT secret.

### 3. Start MySQL

```bash
docker compose up -d
```

This starts MySQL and creates the configured database.

### 4. Run the application

```bash
./gradlew bootRun
```

The application starts at:

`http://localhost:8080`

Hibernate manages the database schema using the configured JPA settings.

## API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/auth/register` | Register a customer | Public |
| POST | `/auth/login` | Authenticate and receive a JWT | Public |

Example registration request:

```json
{
  "email": "customer@example.com",
  "password": "StrongPass123",
  "firstName": "John",
  "lastName": "Doe"
}
```

The role defaults to `CUSTOMER`. Public registration does not permit ADMIN self-assignment.

### Video Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/videos` | Browse available videos | CUSTOMER, ADMIN |
| GET | `/videos/{id}` | Get video by ID | CUSTOMER, ADMIN |
| POST | `/videos` | Create a video | ADMIN |
| PUT | `/videos/{id}` | Update a video | ADMIN |
| DELETE | `/videos/{id}` | Delete a video | ADMIN |

Example video request:

```json
{
  "title": "Interstellar",
  "director": "Christopher Nolan",
  "genre": "Sci-Fi"
}
```

New videos are created as available.

### Rental Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/videos/{videoId}/rent` | Rent a video | CUSTOMER |
| POST | `/videos/{videoId}/return` | Return a rented video | CUSTOMER |

Rental rules:

- A customer can have a maximum of two active rentals.
- A customer cannot rent the same video twice while its rental is active.
- Only available videos can be rented.
- Returning a video makes it available again.
- Rental operations are performed for the authenticated customer.

## Authentication

The application uses stateless JWT authentication.

First, register and log in using `/auth/register` and `/auth/login`. The login response provides a JWT.

For protected endpoints, include the token in the request header:

```http
Authorization: Bearer <your-jwt-token>
```

An ADMIN account is provisioned automatically at application startup using `ADMIN_EMAIL` and `ADMIN_PASSWORD` from the environment.

## Authorization Rules

| Operation | CUSTOMER | ADMIN |
|---|---|---|
| Register | Yes | Yes |
| Login | Yes | Yes |
| Browse available videos | Yes | Yes |
| View video by ID | Yes | Yes |
| Create video | No | Yes |
| Update video | No | Yes |
| Delete video | No | Yes |
| Rent video | Yes | No |
| Return video | Yes | No |

## HTTP Status Codes

| Status | Meaning |
|---|---|
| 200 | Request successful |
| 201 | Resource created |
| 204 | Resource deleted successfully |
| 400 | Invalid request or validation failure |
| 401 | Authentication required or invalid credentials |
| 403 | Insufficient permissions |
| 404 | Resource not found |
| 409 | Business rule conflict, such as duplicate email or rental limit |

## Project Structure

```text
src/main/java/com/example/rentvideo/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
├── security/
└── service/
```

The application follows a layered architecture:

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic.
- **Repository:** Handles database operations.
- **Entity:** Represents database entities.
- **DTO:** Defines request and response objects.
- **Security:** Handles JWT authentication and authorization.
- **Exception:** Centralizes API exception handling.

## Stopping MySQL

```bash
docker compose down
```

To remove the database volume and all persisted MySQL data:

```bash
docker compose down -v
```

## License

This project was developed as a take-home assignment.