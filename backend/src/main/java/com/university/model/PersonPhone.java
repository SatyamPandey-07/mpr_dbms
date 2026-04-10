package com.university.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonPhone {
    private Integer personId;
    private String phoneNumber;
    private Person person; // Optional: to hold person details
}
