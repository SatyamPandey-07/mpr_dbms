package com.university.dao;

import com.university.model.PersonPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonPhoneDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class PersonPhoneRowMapper implements RowMapper<PersonPhone> {
        @Override
        public PersonPhone mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersonPhone personPhone = new PersonPhone();
            personPhone.setPersonId(rs.getInt("person_id"));
            personPhone.setPhoneNumber(rs.getString("phone_number"));
            return personPhone;
        }
    }

    public List<PersonPhone> findByPersonId(Integer personId) {
        String sql = "SELECT * FROM PERSON_PHONE WHERE person_id = ?";
        return jdbcTemplate.query(sql, new PersonPhoneRowMapper(), personId);
    }

    public int save(PersonPhone personPhone) {
        String sql = "INSERT INTO PERSON_PHONE (person_id, phone_number) VALUES (?, ?)";
        return jdbcTemplate.update(sql, personPhone.getPersonId(), personPhone.getPhoneNumber());
    }

    public int update(PersonPhone personPhone) {
        String sql = "UPDATE PERSON_PHONE SET phone_number = ? WHERE person_id = ? AND phone_number = ?";
        return jdbcTemplate.update(sql, personPhone.getPhoneNumber(), personPhone.getPersonId(), personPhone.getPhoneNumber());
    }

    public int delete(Integer personId, String phoneNumber) {
        String sql = "DELETE FROM PERSON_PHONE WHERE person_id = ? AND phone_number = ?";
        return jdbcTemplate.update(sql, personId, phoneNumber);
    }

    public int deleteAllByPersonId(Integer personId) {
        String sql = "DELETE FROM PERSON_PHONE WHERE person_id = ?";
        return jdbcTemplate.update(sql, personId);
    }
}
