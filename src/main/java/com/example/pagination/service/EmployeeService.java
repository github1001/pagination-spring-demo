package com.example.pagination.service;

import com.example.pagination.dto.*;
import com.example.pagination.entity.Department;
import com.example.pagination.entity.Employee;
import com.example.pagination.exception.ResourceNotFoundException;
import com.example.pagination.repository.DepartmentRepository;
import com.example.pagination.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Page<EmployeeResponse> search(String keyword, String department, Pageable pageable) {
        return employeeRepository.search(blankToNull(keyword), blankToNull(department), pageable)
                .map(this::toResponse);
    }

    public EmployeeResponse findById(Long id) {
        return toResponse(findEmployee(id));
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        Department department = findDepartment(request.departmentId());
        Employee employee = new Employee(request.name(), request.email(), request.title(), department);
        return toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse replace(Long id, EmployeeRequest request) {
        Employee employee = findEmployee(id);
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setTitle(request.title());
        employee.setDepartment(findDepartment(request.departmentId()));
        return toResponse(employee);
    }

    @Transactional
    public EmployeeResponse patch(Long id, EmployeePatchRequest request) {
        Employee employee = findEmployee(id);
        if (request.name() != null) employee.setName(request.name());
        if (request.email() != null) employee.setEmail(request.email());
        if (request.title() != null) employee.setTitle(request.title());
        if (request.departmentId() != null) employee.setDepartment(findDepartment(request.departmentId()));
        return toResponse(employee);
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.delete(findEmployee(id));
    }

    public List<DepartmentSummary> departmentSummary() {
        return employeeRepository.departmentSummary();
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getTitle(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName()
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
