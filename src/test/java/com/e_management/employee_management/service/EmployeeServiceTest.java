package com.e_management.employee_management.service;

import com.e_management.employee_management.dto.EmployeeRequest;
import com.e_management.employee_management.exception.DuplicateEmailException;
import com.e_management.employee_management.exception.EmployeeNotFoundException;
import com.e_management.employee_management.model.Employee;
import com.e_management.employee_management.model.EmployeeStatus;
import com.e_management.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequest validRequest() {
        return new EmployeeRequest("Alice Ngu", "alice@example.com", null,
                LocalDate.of(1995, 4, 23), LocalDate.of(2026, 1, 10),
                "Engineering", new BigDecimal("450000.00"), EmployeeStatus.ACTIVE);
    }

    @Test
    void createEmployee_savesEmployee_whenEmailIsNew() {
        when(employeeRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.createEmployee(validRequest());

        assertThat(result.getFullName()).isEqualTo("Alice Ngu");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_throwsDuplicateEmail_whenEmailExists() {
        when(employeeRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(validRequest()))
                .isInstanceOf(DuplicateEmailException.class);

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getEmployeeById_throwsNotFound_whenMissing() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(id))
                .isInstanceOf(EmployeeNotFoundException.class);
    }
}