package com.example.pagination.config;

import com.example.pagination.entity.Department;
import com.example.pagination.entity.Employee;
import com.example.pagination.repository.DepartmentRepository;
import com.example.pagination.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        return args -> {
            if (employeeRepository.count() > 0) return;

            Department engineering = departmentRepository.save(new Department("Engineering"));
            Department finance = departmentRepository.save(new Department("Finance"));
            Department hr = departmentRepository.save(new Department("Human Resources"));

            employeeRepository.save(new Employee("Aisha Rahman", "aisha@example.com", "Software Engineer", engineering));
            employeeRepository.save(new Employee("Ben Tan", "ben@example.com", "Senior Engineer", engineering));
            employeeRepository.save(new Employee("Chloe Lim", "chloe@example.com", "QA Engineer", engineering));
            employeeRepository.save(new Employee("Daniel Wong", "daniel@example.com", "Accountant", finance));
            employeeRepository.save(new Employee("Elena Lee", "elena@example.com", "Finance Analyst", finance));
            employeeRepository.save(new Employee("Farid Hassan", "farid@example.com", "HR Executive", hr));
            employeeRepository.save(new Employee("Grace Ong", "grace@example.com", "Recruiter", hr));
            employeeRepository.save(new Employee("Haris Ismail", "haris@example.com", "Platform Engineer", engineering));
            employeeRepository.save(new Employee("Ivy Chan", "ivy@example.com", "Payroll Specialist", finance));
            employeeRepository.save(new Employee("Jason Ng", "jason@example.com", "Engineering Manager", engineering));
            employeeRepository.save(new Employee("Kavitha Rao", "kavitha@example.com", "People Partner", hr));
            employeeRepository.save(new Employee("Lucas Teh", "lucas@example.com", "Backend Engineer", engineering));
        };
    }
}
