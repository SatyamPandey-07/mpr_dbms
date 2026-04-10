package com.university.controller;

import com.university.dto.ApiResponse;
import com.university.model.Instructor;
import com.university.model.Student;
import com.university.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@CrossOrigin(origins = "*")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping
    public ApiResponse<List<Instructor>> getAllInstructors() {
        return ApiResponse.success(instructorService.getAllInstructors(), "Fetched instructors successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<Instructor> getInstructorById(@PathVariable Integer id) {
        return ApiResponse.success(instructorService.getInstructorById(id), "Fetched instructor successfully");
    }

    @PostMapping
    public ApiResponse<String> addInstructor(@RequestBody Instructor instructor) {
        instructorService.addInstructor(instructor);
        return ApiResponse.success(null, "Instructor added successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<String> updateInstructor(@PathVariable Integer id, @RequestBody Instructor instructor) {
        instructor.setInstructorId(id);
        instructorService.updateInstructor(instructor);
        return ApiResponse.success(null, "Instructor updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteInstructor(@PathVariable Integer id) {
        instructorService.deleteInstructor(id);
        return ApiResponse.success(null, "Instructor deleted successfully");
    }

    @PostMapping("/{instructorId}/advisees/{studentId}")
    public ApiResponse<String> assignAdvisor(@PathVariable Integer instructorId, @PathVariable Integer studentId) {
        instructorService.assignAdvisor(instructorId, studentId);
        return ApiResponse.success(null, "Advisor assigned successfully");
    }

    @PostMapping("/{instructorId}/courses/{courseId}")
    public ApiResponse<String> assignCourse(@PathVariable Integer instructorId, @PathVariable Integer courseId, 
                                          @RequestParam String semester, @RequestParam int year) {
        instructorService.assignCourse(instructorId, courseId, semester, year);
        return ApiResponse.success(null, "Course assigned successfully");
    }

    @GetMapping("/{id}/advisees")
    public ApiResponse<List<Student>> getAdvisees(@PathVariable Integer id) {
        return ApiResponse.success(instructorService.getAdvisees(id), "Fetched advisees successfully");
    }
}
