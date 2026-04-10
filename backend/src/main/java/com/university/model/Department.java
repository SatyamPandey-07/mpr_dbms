package com.university.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private Integer deptId;
    private String name;
    private String office;
    private Integer chairId; // FK to Instructor
}
