# 📰 News Aggregator - Spring Boot Application

This project is a **Spring Boot REST API** for a **News Aggregator application** with **user registration, JWT-based authentication, user preferences management, and external news fetching based on preferences**.

---

## 🚀 Features

✅ User Registration (POST `/api/register`)  
✅ User Login with JWT (POST `/api/login`)  
✅ Secure endpoints using Spring Security  
✅ Store user preferences in an in-memory H2 database  
✅ Update and fetch user news preferences  
✅ Fetch news articles from external APIs based on user preferences  
✅ Input validation using Spring Validation  
✅ Exception handling with structured JSON errors  
✅ Tested with Postman

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- H2 in-memory database
- Lombok
- Gradle
- Postman (for testing)

---

Fetches news articles based on the logged-in user's preferences using external news APIs.

---

## 🗃️ Database

Uses **H2 in-memory database**:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:newsDB`
- Username: `sa`
- Password: (empty)

---

## 🛡️ Security

- JWT-based authentication for secure endpoints.
- CSRF disabled for API usage.
- `/api/register` and `/api/login` are publicly accessible.
- Other endpoints require a valid JWT token in headers:
    ```
    Authorization: Bearer <token>
    ```

---

## 🧪 Testing with Postman

1️⃣ **Register:**
- Method: POST
- URL: `http://localhost:8080/api/register`
- Headers:
    ```
    Content-Type: application/json
    ```
- Body:
    ```json
    {
        "username": "john",
        "password": "password123",
        "preferences": ["technology", "sports"]
    }
    ```

2️⃣ **Login:**
- Method: POST
- URL: `http://localhost:8080/api/login`
- Body:
    ```json
    {
        "username": "john",
        "password": "password123"
    }
    ```
- Copy the returned `token` for use in secured endpoints.

---
