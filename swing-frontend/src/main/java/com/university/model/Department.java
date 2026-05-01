package com.university.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    private Integer deptId;
    private String name;
    private String office;
    private Integer chairId;

    @Override
    public String toString() {
        return name;
    }
}
