package com.university.service;

import com.university.dao.CourseDao;
import com.university.model.Course;
import com.university.model.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    private CourseDao courseDao;

    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    public Course getCourseById(Integer id) {
        return courseDao.findById(id);
    }

    public List<Course> getCoursesByDepartment(Integer deptId) {
        return courseDao.findByDepartment(deptId);
    }

    @Transactional
    public void addCourse(Course course) {
        courseDao.save(course);
    }

    @Transactional
    public void updateCourse(Course course) {
        courseDao.update(course);
    }

    @Transactional
    public void deleteCourse(Integer id) {
        courseDao.delete(id);
    }

    public List<Course> searchCourses(String searchTerm) {
        return courseDao.findBySearchTerm(searchTerm);
    }

    public List<Map<String, Object>> getCoursesWithInstructors() {
        return courseDao.findCoursesWithInstructors();
    }

    public List<Instructor> getInstructorsByCourse(Integer courseId) {
        return courseDao.findInstructorsByCourse(courseId);
    }
}
