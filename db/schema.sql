-- University Management System Schema
-- strictly following Part 3 requirements

CREATE DATABASE IF NOT EXISTS university_db;
USE university_db;

-- 1. PERSON Table
CREATE TABLE PERSON (
    person_id INT AUTO_INCREMENT PRIMARY KEY,
    ssn VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    date_of_birth DATE,
    email VARCHAR(100) UNIQUE
);

-- 2. STUDENT Table
CREATE TABLE STUDENT (
    student_id INT PRIMARY KEY,
    enrollment_year INT,
    major VARCHAR(100),
    FOREIGN KEY (student_id) REFERENCES PERSON(person_id) ON DELETE CASCADE
);

-- 3. INSTRUCTOR Table
CREATE TABLE INSTRUCTOR (
    instructor_id INT PRIMARY KEY,
    rank_value VARCHAR(50), -- 'rank' is a reserved keyword in some SQL versions
    salary DECIMAL(10, 2),
    FOREIGN KEY (instructor_id) REFERENCES PERSON(person_id) ON DELETE CASCADE
);

-- 4. DEPARTMENT Table
CREATE TABLE DEPARTMENT (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    office VARCHAR(50),
    chair_id INT NULL,
    FOREIGN KEY (chair_id) REFERENCES INSTRUCTOR(instructor_id) ON DELETE SET NULL
);

-- 5. COURSE Table
CREATE TABLE COURSE (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    credits INT,
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES DEPARTMENT(dept_id) ON DELETE CASCADE
);

-- 6. TEACHES Table (M:N)
CREATE TABLE TEACHES (
    instructor_id INT,
    course_id INT,
    semester VARCHAR(20),
    year INT,
    PRIMARY KEY (instructor_id, course_id, semester, year),
    FOREIGN KEY (instructor_id) REFERENCES INSTRUCTOR(instructor_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES COURSE(course_id) ON DELETE CASCADE
);

-- 7. ADVISES Table (1:N or M:N, usually student has 1 advisor)
CREATE TABLE ADVISES (
    instructor_id INT,
    student_id INT PRIMARY KEY,
    FOREIGN KEY (instructor_id) REFERENCES INSTRUCTOR(instructor_id) ON DELETE SET NULL,
    FOREIGN KEY (student_id) REFERENCES STUDENT(student_id) ON DELETE CASCADE
);

-- 8. OFFERS Table (Relationship between Dept and Course)
CREATE TABLE OFFERS (
    dept_id INT,
    course_id INT PRIMARY KEY,
    FOREIGN KEY (dept_id) REFERENCES DEPARTMENT(dept_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES COURSE(course_id) ON DELETE CASCADE
);

-- 9. CHAIRS Table (Relationship between Instructor and Dept)
CREATE TABLE CHAIRS (
    instructor_id INT,
    dept_id INT PRIMARY KEY,
    FOREIGN KEY (instructor_id) REFERENCES INSTRUCTOR(instructor_id) ON DELETE CASCADE,
    FOREIGN KEY (dept_id) REFERENCES DEPARTMENT(dept_id) ON DELETE CASCADE
);

-- 10. AFFILIATED_WITH Table (Instructor and Dept)
CREATE TABLE AFFILIATED_WITH (
    instructor_id INT PRIMARY KEY,
    dept_id INT,
    FOREIGN KEY (instructor_id) REFERENCES INSTRUCTOR(instructor_id) ON DELETE CASCADE,
    FOREIGN KEY (dept_id) REFERENCES DEPARTMENT(dept_id) ON DELETE CASCADE
);

-- 11. PERSON_PHONE Table (Person and Phone)
CREATE TABLE PERSON_PHONE (
    person_id INT,
    phone_number VARCHAR(20),
    PRIMARY KEY (person_id, phone_number),
    FOREIGN KEY (person_id) REFERENCES PERSON(person_id) ON DELETE CASCADE
);

-- Sample Data for Demo
INSERT INTO PERSON (ssn, first_name, last_name, address, date_of_birth, email) VALUES 
('111-222-333', 'Alice', 'Smith', '123 Main St', '2000-05-15', 'alice@university.edu'),
('444-555-666', 'Bob', 'Johnson', '456 Oak Ave', '1985-10-20', 'bob@university.edu');

INSERT INTO STUDENT (student_id, enrollment_year, major) VALUES (1, 2022, 'Computer Science');
INSERT INTO INSTRUCTOR (instructor_id, rank_value, salary) VALUES (2, 'Professor', 95000.00);

INSERT INTO DEPARTMENT (name, office, chair_id) VALUES ('CS', 'Room 302', 2);
UPDATE DEPARTMENT SET chair_id = 2 WHERE dept_id = 1;

INSERT INTO COURSE (title, credits, dept_id) VALUES ('Database Systems', 4, 1);
INSERT INTO OFFERS (dept_id, course_id) VALUES (1, 1);
