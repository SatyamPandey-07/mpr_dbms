package com.university.controller;

import com.university.dto.ApiResponse;
import com.university.model.Instructor;
import com.university.model.Student;
import com.university.service.InstructorService;
import com.university.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @GetMapping
    public ApiResponse<List<Student>> getAllStudents() {
        return ApiResponse.success(studentService.getAllStudents(), "Fetched students successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<Student> getStudentById(@PathVariable Integer id) {
        return ApiResponse.success(studentService.getStudentById(id), "Fetched student successfully");
    }

    @PostMapping
    public ApiResponse<String> addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
        return ApiResponse.success(null, "Student added successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<String> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        student.setStudentId(id);
        studentService.updateStudent(student);
        return ApiResponse.success(null, "Student updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ApiResponse.success(null, "Student deleted successfully");
    }

    @GetMapping("/search")
    public ApiResponse<List<Student>> searchStudents(@RequestParam String q) {
        return ApiResponse.success(studentService.searchStudents(q), "Search completed successfully");
    }

    @GetMapping("/with-advisors")
    public ApiResponse<List<Map<String, Object>>> getStudentsWithAdvisors() {
        List<Student> students = studentService.getStudentsWithAdvisors();
        List<Map<String, Object>> result = students.stream().map(student -> {
            Map<String, Object> map = new HashMap<>();
            map.put("student", student);
            Integer advisorId = studentService.getStudentAdvisorId(student.getStudentId());
            if (advisorId != null) {
                Instructor advisor = instructorService.getInstructorById(advisorId);
                map.put("advisor", advisor);
            } else {
                map.put("advisor", null);
            }
            return map;
        }).toList();
        return ApiResponse.success(result, "Fetched students with advisors successfully");
    }
}
