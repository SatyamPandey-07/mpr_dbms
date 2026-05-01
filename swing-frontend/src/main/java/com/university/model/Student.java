package com.university.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    private Integer studentId;
    private Integer enrollmentYear;
    private String major;
    private Person person;
}
