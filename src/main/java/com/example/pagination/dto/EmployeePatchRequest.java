package com.example.pagination.dto;

import jakarta.validation.constraints.Email;

public record EmployeePatchRequest(
        String name,
        @Email String email,
        String title,
        Long departmentId
) {
}
