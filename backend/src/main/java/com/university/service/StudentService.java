package com.university.service;

import com.university.dao.StudentDao;
import com.university.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentDao.findById(id);
    }

    @Transactional
    public void addStudent(Student student) {
        studentDao.save(student);
    }

    @Transactional
    public void updateStudent(Student student) {
        studentDao.update(student);
    }

    @Transactional
    public void deleteStudent(Integer id) {
        studentDao.delete(id);
    }

    public List<Student> searchStudents(String searchTerm) {
        return studentDao.findBySearchTerm(searchTerm);
    }

    public List<Student> getStudentsWithAdvisors() {
        return studentDao.findWithAdvisors();
    }

    public Integer getStudentAdvisorId(Integer studentId) {
        return studentDao.getAdvisorId(studentId);
    }
}
