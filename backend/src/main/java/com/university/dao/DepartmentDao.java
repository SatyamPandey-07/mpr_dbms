package com.university.dao;

import com.university.model.Department;
import com.university.model.Instructor;
import com.university.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class DepartmentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Department> findAll() {
        String sql = "SELECT * FROM DEPARTMENT";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Department.class));
    }

    public Department findById(Integer id) {
        String sql = "SELECT * FROM DEPARTMENT WHERE dept_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Department.class), id);
    }

    public int save(Department dept) {
        String sql = "INSERT INTO DEPARTMENT (name, office, chair_id) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, dept.getName(), dept.getOffice(), dept.getChairId());
    }

    public int update(Department dept) {
        String sql = "UPDATE DEPARTMENT SET name=?, office=?, chair_id=? WHERE dept_id=?";
        return jdbcTemplate.update(sql, dept.getName(), dept.getOffice(), dept.getChairId(), dept.getDeptId());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM DEPARTMENT WHERE dept_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Integer getChairId(Integer deptId) {
        String sql = "SELECT chair_id FROM DEPARTMENT WHERE dept_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, deptId);
    }

    public List<Instructor> findInstructorsByDepartment(Integer deptId) {
        String sql = "SELECT i.*, p.* FROM INSTRUCTOR i " +
                    "JOIN PERSON p ON i.instructor_id = p.person_id " +
                    "JOIN AFFILIATED_WITH aw ON i.instructor_id = aw.instructor_id " +
                    "WHERE aw.dept_id = ?";
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
        }, deptId);
    }
}
