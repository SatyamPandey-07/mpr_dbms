package com.university.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Integer personId;
    private String ssn;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate dateOfBirth;
    private String email;
}
