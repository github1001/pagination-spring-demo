package com.example.pagination.service;

import com.example.pagination.dto.EmployeeRequest;
import com.example.pagination.entity.Department;
import com.example.pagination.repository.DepartmentRepository;
import com.example.pagination.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock EmployeeRepository employeeRepository;
    @Mock DepartmentRepository departmentRepository;
    @InjectMocks EmployeeService employeeService;

    @Test
    void createMapsEntityToResponse() {
        Department department = new Department("Engineering");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = employeeService.create(
                new EmployeeRequest("Alice", "alice@test.com", "Developer", 1L));

        assertThat(response.name()).isEqualTo("Alice");
        assertThat(response.departmentName()).isEqualTo("Engineering");
    }
}
