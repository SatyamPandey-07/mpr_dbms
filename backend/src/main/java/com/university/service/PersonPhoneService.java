package com.university.service;

import com.university.dao.PersonPhoneDao;
import com.university.model.PersonPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonPhoneService {

    @Autowired
    private PersonPhoneDao personPhoneDao;

    public List<PersonPhone> getPhonesByPersonId(Integer personId) {
        return personPhoneDao.findByPersonId(personId);
    }

    @Transactional
    public void addPhone(PersonPhone personPhone) {
        personPhoneDao.save(personPhone);
    }

    @Transactional
    public void updatePhone(PersonPhone personPhone, String oldPhoneNumber) {
        personPhoneDao.delete(personPhone.getPersonId(), oldPhoneNumber);
        personPhoneDao.save(personPhone);
    }

    @Transactional
    public void deletePhone(Integer personId, String phoneNumber) {
        personPhoneDao.delete(personId, phoneNumber);
    }

    @Transactional
    public void deleteAllPhones(Integer personId) {
        personPhoneDao.deleteAllByPersonId(personId);
    }
}
