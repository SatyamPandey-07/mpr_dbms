package com.university.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer studentId;
    private Integer enrollmentYear;
    private String major;
    private Person person; // Composition to hold Person details
}
