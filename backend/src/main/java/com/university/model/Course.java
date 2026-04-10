package com.university.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Integer courseId;
    private String title;
    private Integer credits;
    private Integer deptId;
}
