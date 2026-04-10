package com.university.controller;

import com.university.dto.ApiResponse;
import com.university.model.Department;
import com.university.model.Instructor;
import com.university.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ApiResponse<List<Department>> getAllDepartments() {
        return ApiResponse.success(departmentService.getAllDepartments(), "Fetched departments successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<Department> getDepartmentById(@PathVariable Integer id) {
        return ApiResponse.success(departmentService.getDepartmentById(id), "Fetched department successfully");
    }

    @PostMapping
    public ApiResponse<String> addDepartment(@RequestBody Department department) {
        departmentService.addDepartment(department);
        return ApiResponse.success(null, "Department added successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<String> updateDepartment(@PathVariable Integer id, @RequestBody Department department) {
        department.setDeptId(id);
        departmentService.updateDepartment(department);
        return ApiResponse.success(null, "Department updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ApiResponse.success(null, "Department deleted successfully");
    }

    @GetMapping("/{id}/chair")
    public ApiResponse<Integer> getDepartmentChair(@PathVariable Integer id) {
        return ApiResponse.success(departmentService.getDepartmentChair(id), "Fetched department chair successfully");
    }

    @GetMapping("/{id}/instructors")
    public ApiResponse<List<Instructor>> getInstructorsByDepartment(@PathVariable Integer id) {
        return ApiResponse.success(departmentService.getInstructorsByDepartment(id), "Fetched instructors for department successfully");
    }
}
