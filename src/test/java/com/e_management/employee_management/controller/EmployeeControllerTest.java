package com.e_management.employee_management.controller;

import com.e_management.employee_management.dto.EmployeeRequest;
import com.e_management.employee_management.exception.EmployeeNotFoundException;
import com.e_management.employee_management.model.Employee;
import com.e_management.employee_management.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    private static final String VALID_JSON = """
            {
              "fullName": "Alice Ngu",
              "email": "alice@example.com",
              "dateOfBirth": "1995-04-23",
              "hireDate": "2026-01-10",
              "department": "Engineering",
              "salary": 450000.00,
              "status": "ACTIVE"
            }
            """;

    @Test
    void createEmployee_returns201_whenRequestIsValid() throws Exception {
        Employee saved = new Employee();
        saved.setFullName("Alice Ngu");
        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Alice Ngu"));
    }

    @Test
    void createEmployee_returns400WithFieldErrors_whenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void getEmployee_returns404WithErrorBody_whenMissing() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeService.getEmployeeById(id)).thenThrow(new EmployeeNotFoundException(id));

        mockMvc.perform(get("/employees/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Employee not found with id: " + id));
    }
}