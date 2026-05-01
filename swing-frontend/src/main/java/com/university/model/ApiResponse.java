package com.university.model;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
}
