package com.university.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    
    @NotBlank(message = "SSN is required")
    private String ssn;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String address;
    
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotNull(message = "Enrollment year is required")
    private Integer enrollmentYear;
    
    @NotBlank(message = "Major is required")
    private String major;
}
