package com.example.pagination.dto;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        String title,
        Long departmentId,
        String departmentName
) {
}
