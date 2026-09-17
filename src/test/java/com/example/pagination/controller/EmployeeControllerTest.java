package com.example.pagination.controller;

import com.example.pagination.dto.EmployeeResponse;
import com.example.pagination.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean EmployeeService employeeService;

    @Test
    void searchReturnsPage() throws Exception {
        when(employeeService.search(any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(
                        new EmployeeResponse(1L, "Alice", "alice@test.com", "Developer", 1L, "Engineering")
                )));

        mockMvc.perform(get("/api/employees/search").param("page", "0").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Alice"));
    }
}
