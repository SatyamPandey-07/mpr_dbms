package com.university.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {
    private Integer instructorId;
    private String rank;
    private BigDecimal salary;
    private Person person;
}
