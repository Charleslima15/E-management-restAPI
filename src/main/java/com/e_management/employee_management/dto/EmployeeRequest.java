package com.e_management.employee_management.dto;

import com.e_management.employee_management.model.EmployeeStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        String phoneNumber,
        @NotNull @Past LocalDate dateOfBirth,
        @NotNull @PastOrPresent LocalDate hireDate,
        @NotBlank String department,
        @NotNull @Positive BigDecimal salary,
        @NotNull EmployeeStatus status
) {
}