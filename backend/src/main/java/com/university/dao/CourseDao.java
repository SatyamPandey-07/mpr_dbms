package com.university.dao;

import com.university.model.Course;
import com.university.model.Instructor;
import com.university.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CourseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Course> findAll() {
        String sql = "SELECT * FROM COURSE";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class));
    }

    public Course findById(Integer id) {
        String sql = "SELECT * FROM COURSE WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Course.class), id);
    }

    public List<Course> findByDepartment(Integer deptId) {
        String sql = "SELECT * FROM COURSE WHERE dept_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class), deptId);
    }

    public int save(Course course) {
        String sql = "INSERT INTO COURSE (title, credits, dept_id) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, course.getTitle(), course.getCredits(), course.getDeptId());
    }

    public int update(Course course) {
        String sql = "UPDATE COURSE SET title=?, credits=?, dept_id=? WHERE course_id=?";
        return jdbcTemplate.update(sql, course.getTitle(), course.getCredits(), course.getDeptId(), course.getCourseId());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM COURSE WHERE course_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Course> findBySearchTerm(String searchTerm) {
        String sql = "SELECT * FROM COURSE WHERE title LIKE ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class), "%" + searchTerm + "%");
    }

    public List<Map<String, Object>> findCoursesWithInstructors() {
        String sql = "SELECT c.*, i.instructor_id, p.first_name, p.last_name, t.semester, t.year " +
                    "FROM COURSE c " +
                    "LEFT JOIN TEACHES t ON c.course_id = t.course_id " +
                    "LEFT JOIN INSTRUCTOR i ON t.instructor_id = i.instructor_id " +
                    "LEFT JOIN PERSON p ON i.instructor_id = p.person_id";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Instructor> findInstructorsByCourse(Integer courseId) {
        String sql = "SELECT i.*, p.* FROM INSTRUCTOR i " +
                    "JOIN PERSON p ON i.instructor_id = p.person_id " +
                    "JOIN TEACHES t ON i.instructor_id = t.instructor_id " +
                    "WHERE t.course_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
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
        }, courseId);
    }
}
