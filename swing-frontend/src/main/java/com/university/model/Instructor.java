package com.university.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instructor {
    private Integer instructorId;
    private String rank;
    private Double salary;
    private Person person;

    @Override
    public String toString() {
        if (person != null) {
            return person.getFirstName() + " " + person.getLastName() + " (" + rank + ")";
        }
        return "Instructor #" + instructorId;
    }
}
