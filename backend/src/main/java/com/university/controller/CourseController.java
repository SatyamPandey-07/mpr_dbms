package com.university.controller;

import com.university.dto.ApiResponse;
import com.university.model.Course;
import com.university.model.Instructor;
import com.university.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ApiResponse<List<Course>> getAllCourses() {
        return ApiResponse.success(courseService.getAllCourses(), "Fetched courses successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<Course> getCourseById(@PathVariable Integer id) {
        return ApiResponse.success(courseService.getCourseById(id), "Fetched course successfully");
    }

    @GetMapping("/department/{deptId}")
    public ApiResponse<List<Course>> getCoursesByDepartment(@PathVariable Integer deptId) {
        return ApiResponse.success(courseService.getCoursesByDepartment(deptId), "Fetched courses for department successfully");
    }

    @PostMapping
    public ApiResponse<String> addCourse(@RequestBody Course course) {
        courseService.addCourse(course);
        return ApiResponse.success(null, "Course added successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<String> updateCourse(@PathVariable Integer id, @RequestBody Course course) {
        course.setCourseId(id);
        courseService.updateCourse(course);
        return ApiResponse.success(null, "Course updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return ApiResponse.success(null, "Course deleted successfully");
    }

    @GetMapping("/search")
    public ApiResponse<List<Course>> searchCourses(@RequestParam String q) {
        return ApiResponse.success(courseService.searchCourses(q), "Search completed successfully");
    }

    @GetMapping("/with-instructors")
    public ApiResponse<List<Map<String, Object>>> getCoursesWithInstructors() {
        return ApiResponse.success(courseService.getCoursesWithInstructors(), "Fetched courses with instructors successfully");
    }

    @GetMapping("/{id}/instructors")
    public ApiResponse<List<Instructor>> getInstructorsByCourse(@PathVariable Integer id) {
        return ApiResponse.success(courseService.getInstructorsByCourse(id), "Fetched instructors for course successfully");
    }
}
