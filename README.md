# University Management System (MPR DBMS)

A full-stack University Management System built with Spring Boot (JDBC) + React (Vite) + MySQL.

It supports management of students, instructors, departments, courses, and core university relationships like advising and course assignments.

## Features

- CRUD for Students, Instructors, Departments, and Courses
- Advisor assignment (Instructor -> Student)
- Course assignment (Instructor -> Course with semester/year)
- Search support on key entities
- Toast notifications and loading states on frontend
- MySQL relational schema with constraints and sample data

## Tech Stack

- Backend: Java 17, Spring Boot 3.2.2, JDBC, HikariCP, Lombok
- Frontend: React 18, Vite 5, Tailwind CSS, Axios
- Database: MySQL 8+

## Project Structure

```text
mpr/
  backend/    Spring Boot REST API
  db/         SQL schema + seed data
  frontend/   React Vite app
```

## Prerequisites

- Java 17+
- MySQL 8+
- Node.js 18+ and npm
- Maven 3.9+ (or run backend from IDE)

## Setup

### 1. Database

Run the SQL script:

```bash
mysql -u root -p < db/schema.sql
```

This creates `university_db` with sample records.

### 2. Backend

Update DB credentials if needed in:

- `backend/src/main/resources/application.properties`

Then run backend:

```bash
cd backend
mvn spring-boot:run
```

If Maven is not available in PATH, run from IDE using:

- `backend/src/main/java/com/university/UniversityApplication.java`

Backend default URL: `http://localhost:8080`

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend default URL: `http://localhost:3000`

Vite proxies `/api` to backend `http://localhost:8080`.

## API Base

- Base path: `/api`
- Example endpoints:
  - `/api/students`
  - `/api/instructors`
  - `/api/departments`
  - `/api/courses`

## Notes

- The backend uses JDBC DAOs (no Hibernate/JPA).
- Error handling is centralized via a global exception handler.
- The SQL schema includes foreign keys and delete rules for relationships.

## License

MIT
