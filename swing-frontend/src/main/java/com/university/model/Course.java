package com.university.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    private Integer courseId;
    private String title;
    private Integer credits;
    private Integer deptId;

    @Override
    public String toString() {
        return title;
    }
}
