package com.university.service;

import com.university.dao.DepartmentDao;
import com.university.model.Department;
import com.university.model.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    public List<Department> getAllDepartments() {
        return departmentDao.findAll();
    }

    public Department getDepartmentById(Integer id) {
        return departmentDao.findById(id);
    }

    @Transactional
    public void addDepartment(Department department) {
        departmentDao.save(department);
    }

    @Transactional
    public void updateDepartment(Department department) {
        departmentDao.update(department);
    }

    @Transactional
    public void deleteDepartment(Integer id) {
        departmentDao.delete(id);
    }

    public Integer getDepartmentChair(Integer departmentId) {
        return departmentDao.getChairId(departmentId);
    }

    public List<Instructor> getInstructorsByDepartment(Integer departmentId) {
        return departmentDao.findInstructorsByDepartment(departmentId);
    }
}
