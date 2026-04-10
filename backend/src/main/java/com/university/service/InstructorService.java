package com.university.service;

import com.university.dao.InstructorDao;
import com.university.model.Instructor;
import com.university.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstructorService {

    @Autowired
    private InstructorDao instructorDao;

    public List<Instructor> getAllInstructors() {
        return instructorDao.findAll();
    }

    public Instructor getInstructorById(Integer id) {
        return instructorDao.findById(id);
    }

    @Transactional
    public void addInstructor(Instructor instructor) {
        instructorDao.save(instructor);
    }

    @Transactional
    public void updateInstructor(Instructor instructor) {
        instructorDao.update(instructor);
    }

    @Transactional
    public void deleteInstructor(Integer id) {
        instructorDao.delete(id);
    }

    @Transactional
    public void assignAdvisor(Integer instructorId, Integer studentId) {
        instructorDao.assignAdvisor(instructorId, studentId);
    }

    @Transactional
    public void assignCourse(Integer instructorId, Integer courseId, String semester, int year) {
        instructorDao.assignCourse(instructorId, courseId, semester, year);
    }

    public List<Student> getAdvisees(Integer instructorId) {
        return instructorDao.getAdvisees(instructorId);
    }
}
