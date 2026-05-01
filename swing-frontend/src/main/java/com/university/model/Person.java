package com.university.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private Integer personId;
    private String ssn;
    private String firstName;
    private String lastName;
    private String address;
    private String dateOfBirth;
    private String email;
}
