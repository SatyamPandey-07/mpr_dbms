# LEARN-UNIVERSITY Management System

University management system with:

- MySQL database schema and seed data
- Spring Boot backend REST API
- Swing desktop frontend (primary desktop client)
- React frontend (optional web client)

## Project Structure

```text
mpr/
  backend/          Spring Boot REST API
  db/               SQL schema and sample data
  swing-frontend/   Java Swing desktop app
  frontend/         React + Vite web app (optional)
```

## Tech Stack

- Java 17
- Spring Boot 3.2.2 (JDBC + HikariCP)
- MySQL 8+
- Swing (FlatLaf + MigLayout + Gson)
- React/Vite (optional)

## Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8+
- Node.js 18+ (only for `frontend/`)

## Quick Start (Swing + Backend + DB)

Run in this order.

### 1. Initialize Database

```bash
mysql -u root -p < db/schema.sql
```

This creates `university_db` and inserts sample records.

### 2. Configure Backend DB Credentials

Edit:

- `backend/src/main/resources/application.properties`

Default currently uses:

- URL: `jdbc:mysql://localhost:3306/university_db?...`
- Username: `root`
- Password: `saam`

Update username/password to your local MySQL values if needed.

### 3. Run Backend API

```bash
cd backend
mvn spring-boot:run
```

Backend base URL:

- `http://localhost:8080`
- API base: `http://localhost:8080/api`

### 4. Run Swing Frontend

Open a new terminal:

```bash
cd swing-frontend
mvn compile exec:java
```

The Swing app calls backend endpoints through:

- `http://localhost:8080/api`

If dashboard counts show `N/A` or don't load, verify backend is running first.

## Optional: Run React Web Frontend

```bash
cd frontend
npm install
npm run dev
```

Vite dev server runs on `http://localhost:3000` and proxies `/api` to backend.

## Common Commands

### Backend

```bash
cd backend
mvn clean test
mvn clean package
```

### Swing Frontend

```bash
cd swing-frontend
mvn clean compile
mvn exec:java
```

## API Endpoints (Examples)

- `/api/students`
- `/api/instructors`
- `/api/departments`
- `/api/courses`

## Notes

- Backend uses JDBC DAOs (no JPA/Hibernate).
- SQL schema includes key relations such as advises, teaches, offers, chairs, affiliated_with, and person_phone.
- Swing and React frontends both depend on the backend API being available.
