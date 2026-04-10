package com.university.dao;

import com.university.model.Person;
import com.university.model.Instructor;
import com.university.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InstructorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class InstructorRowMapper implements RowMapper<Instructor> {
        @Override
        public Instructor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setPersonId(rs.getInt("person_id"));
            person.setSsn(rs.getString("ssn"));
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            person.setAddress(rs.getString("address"));
            person.setDateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null);
            person.setEmail(rs.getString("email"));

            Instructor instructor = new Instructor();
            instructor.setInstructorId(rs.getInt("instructor_id"));
            instructor.setRank(rs.getString("rank_value"));
            instructor.setSalary(rs.getBigDecimal("salary"));
            instructor.setPerson(person);
            return instructor;
        }
    }

    public Instructor findById(Integer id) {
        String sql = "SELECT * FROM INSTRUCTOR i JOIN PERSON p ON i.instructor_id = p.person_id WHERE i.instructor_id = ?";
        return jdbcTemplate.queryForObject(sql, new InstructorRowMapper(), id);
    }

    public List<Instructor> findAll() {
        String sql = "SELECT * FROM INSTRUCTOR i JOIN PERSON p ON i.instructor_id = p.person_id";
        return jdbcTemplate.query(sql, new InstructorRowMapper());
    }

    public int save(Instructor instructor) {
        // 1. Save to PERSON first
        String personSql = "INSERT INTO PERSON (ssn, first_name, last_name, address, date_of_birth, email) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(personSql, new String[]{"person_id"});
            ps.setString(1, instructor.getPerson().getSsn());
            ps.setString(2, instructor.getPerson().getFirstName());
            ps.setString(3, instructor.getPerson().getLastName());
            ps.setString(4, instructor.getPerson().getAddress());
            ps.setDate(5, instructor.getPerson().getDateOfBirth() != null ? Date.valueOf(instructor.getPerson().getDateOfBirth()) : null);
            ps.setString(6, instructor.getPerson().getEmail());
            return ps;
        }, keyHolder);

        int personId = keyHolder.getKey().intValue();

        // 2. Save to INSTRUCTOR
        String instructorSql = "INSERT INTO INSTRUCTOR (instructor_id, rank_value, salary) VALUES (?, ?, ?)";
        return jdbcTemplate.update(instructorSql, personId, instructor.getRank(), instructor.getSalary());
    }

    public int update(Instructor instructor) {
        // 1. Update PERSON
        String personSql = "UPDATE PERSON SET ssn=?, first_name=?, last_name=?, address=?, date_of_birth=?, email=? WHERE person_id=?";
        jdbcTemplate.update(personSql, 
            instructor.getPerson().getSsn(), 
            instructor.getPerson().getFirstName(), 
            instructor.getPerson().getLastName(), 
            instructor.getPerson().getAddress(), 
            instructor.getPerson().getDateOfBirth() != null ? Date.valueOf(instructor.getPerson().getDateOfBirth()) : null,
            instructor.getPerson().getEmail(), 
            instructor.getInstructorId());

        // 2. Update INSTRUCTOR
        String instructorSql = "UPDATE INSTRUCTOR SET rank_value=?, salary=? WHERE instructor_id=?";
        return jdbcTemplate.update(instructorSql, instructor.getRank(), instructor.getSalary(), instructor.getInstructorId());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM PERSON WHERE person_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Relationship: Assign advisor
    public int assignAdvisor(Integer instructorId, Integer studentId) {
        String sql = "INSERT INTO ADVISES (instructor_id, student_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE instructor_id = values(instructor_id)";
        return jdbcTemplate.update(sql, instructorId, studentId);
    }

    // Relationship: Assign course
    public int assignCourse(Integer instructorId, Integer courseId, String semester, int year) {
        String sql = "INSERT INTO TEACHES (instructor_id, course_id, semester, year) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE instructor_id = values(instructor_id)";
        return jdbcTemplate.update(sql, instructorId, courseId, semester, year);
    }

    public List<Student> getAdvisees(Integer instructorId) {
        String sql = "SELECT * FROM STUDENT s JOIN PERSON p ON s.student_id = p.person_id JOIN ADVISES a ON s.student_id = a.student_id WHERE a.instructor_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Person person = new Person();
            person.setPersonId(rs.getInt("person_id"));
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            return new Student(rs.getInt("student_id"), rs.getInt("enrollment_year"), rs.getString("major"), person);
        }, instructorId);
    }
}
