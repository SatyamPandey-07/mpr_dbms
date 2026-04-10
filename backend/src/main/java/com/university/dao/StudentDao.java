package com.university.dao;

import com.university.model.Person;
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
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setPersonId(rs.getInt("person_id"));
            person.setSsn(rs.getString("ssn"));
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            person.setAddress(rs.getString("address"));
            person.setDateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null);
            person.setEmail(rs.getString("email"));

            Student student = new Student();
            student.setStudentId(rs.getInt("student_id"));
            student.setEnrollmentYear(rs.getInt("enrollment_year"));
            student.setMajor(rs.getString("major"));
            student.setPerson(person);
            return student;
        }
    }

    public Student findById(Integer id) {
        String sql = "SELECT * FROM STUDENT s JOIN PERSON p ON s.student_id = p.person_id WHERE s.student_id = ?";
        return jdbcTemplate.queryForObject(sql, new StudentRowMapper(), id);
    }

    public List<Student> findAll() {
        String sql = "SELECT * FROM STUDENT s JOIN PERSON p ON s.student_id = p.person_id";
        return jdbcTemplate.query(sql, new StudentRowMapper());
    }

    public int save(Student student) {
        // 1. Save to PERSON first
        String personSql = "INSERT INTO PERSON (ssn, first_name, last_name, address, date_of_birth, email) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(personSql, new String[]{"person_id"});
            ps.setString(1, student.getPerson().getSsn());
            ps.setString(2, student.getPerson().getFirstName());
            ps.setString(3, student.getPerson().getLastName());
            ps.setString(4, student.getPerson().getAddress());
            ps.setDate(5, student.getPerson().getDateOfBirth() != null ? Date.valueOf(student.getPerson().getDateOfBirth()) : null);
            ps.setString(6, student.getPerson().getEmail());
            return ps;
        }, keyHolder);

        int personId = keyHolder.getKey().intValue();

        // 2. Save to STUDENT
        String studentSql = "INSERT INTO STUDENT (student_id, enrollment_year, major) VALUES (?, ?, ?)";
        return jdbcTemplate.update(studentSql, personId, student.getEnrollmentYear(), student.getMajor());
    }

    public int update(Student student) {
        // 1. Update PERSON
        String personSql = "UPDATE PERSON SET ssn=?, first_name=?, last_name=?, address=?, date_of_birth=?, email=? WHERE person_id=?";
        jdbcTemplate.update(personSql, 
            student.getPerson().getSsn(), 
            student.getPerson().getFirstName(), 
            student.getPerson().getLastName(), 
            student.getPerson().getAddress(), 
            student.getPerson().getDateOfBirth() != null ? Date.valueOf(student.getPerson().getDateOfBirth()) : null,
            student.getPerson().getEmail(), 
            student.getStudentId());

        // 2. Update STUDENT
        String studentSql = "UPDATE STUDENT SET enrollment_year=?, major=? WHERE student_id=?";
        return jdbcTemplate.update(studentSql, student.getEnrollmentYear(), student.getMajor(), student.getStudentId());
    }

    public int delete(Integer id) {
        // PERSON table has ON DELETE CASCADE for STUDENT
        String sql = "DELETE FROM PERSON WHERE person_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Student> findBySearchTerm(String searchTerm) {
        String sql = "SELECT * FROM STUDENT s JOIN PERSON p ON s.student_id = p.person_id " +
                    "WHERE p.first_name LIKE ? OR p.last_name LIKE ? OR s.major LIKE ? OR p.email LIKE ?";
        String pattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, new StudentRowMapper(), pattern, pattern, pattern, pattern);
    }

    public List<Student> findWithAdvisors() {
        String sql = "SELECT s.*, p.*, i.instructor_id as advisor_id, " +
                    "p2.first_name as advisor_first_name, p2.last_name as advisor_last_name " +
                    "FROM STUDENT s " +
                    "JOIN PERSON p ON s.student_id = p.person_id " +
                    "LEFT JOIN ADVISES a ON s.student_id = a.student_id " +
                    "LEFT JOIN INSTRUCTOR i ON a.instructor_id = i.instructor_id " +
                    "LEFT JOIN PERSON p2 ON i.instructor_id = p2.person_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Person person = new Person();
            person.setPersonId(rs.getInt("person_id"));
            person.setSsn(rs.getString("ssn"));
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            person.setAddress(rs.getString("address"));
            person.setDateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null);
            person.setEmail(rs.getString("email"));

            Student student = new Student();
            student.setStudentId(rs.getInt("student_id"));
            student.setEnrollmentYear(rs.getInt("enrollment_year"));
            student.setMajor(rs.getString("major"));
            student.setPerson(person);
            return student;
        });
    }

    public Integer getAdvisorId(Integer studentId) {
        String sql = "SELECT instructor_id FROM ADVISES WHERE student_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, studentId);
        } catch (Exception e) {
            return null;
        }
    }
}
