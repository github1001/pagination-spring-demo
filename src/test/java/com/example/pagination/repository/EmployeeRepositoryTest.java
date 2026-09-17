package com.example.pagination.repository;

import com.example.pagination.entity.Department;
import com.example.pagination.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {
    @Autowired EmployeeRepository employeeRepository;
    @Autowired DepartmentRepository departmentRepository;

    @Test
    void searchFiltersByJoinedDepartmentAndPaginates() {
        Department engineering = departmentRepository.save(new Department("Engineering"));
        Department finance = departmentRepository.save(new Department("Finance"));
        employeeRepository.save(new Employee("Alice", "alice@test.com", "Developer", engineering));
        employeeRepository.save(new Employee("Bob", "bob@test.com", "Analyst", finance));

        var page = employeeRepository.search(null, "Engineer", PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo("Alice");
    }
}
