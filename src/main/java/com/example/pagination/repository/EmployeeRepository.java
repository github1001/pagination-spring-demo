package com.example.pagination.repository;

import com.example.pagination.dto.DepartmentSummary;
import com.example.pagination.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            select e
            from Employee e
            join e.department d
            where (:keyword is null
                   or lower(e.name) like lower(concat('%', :keyword, '%'))
                   or lower(e.email) like lower(concat('%', :keyword, '%'))
                   or lower(e.title) like lower(concat('%', :keyword, '%')))
              and (:department is null
                   or lower(d.name) like lower(concat('%', :department, '%')))
            """)
    Page<Employee> search(@Param("keyword") String keyword,
                          @Param("department") String department,
                          Pageable pageable);

    @Query("""
            select d.name as departmentName, count(e.id) as employeeCount
            from Employee e
            join e.department d
            group by d.id, d.name
            order by d.name
            """)
    List<DepartmentSummary> departmentSummary();
}
