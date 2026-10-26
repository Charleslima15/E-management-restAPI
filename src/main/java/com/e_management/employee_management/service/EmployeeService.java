package com.e_management.employee_management.service;

import com.e_management.employee_management.dto.EmployeeRequest;
import com.e_management.employee_management.exception.DuplicateEmailException;
import com.e_management.employee_management.exception.EmployeeNotFoundException;
import com.e_management.employee_management.model.Employee;
import com.e_management.employee_management.repository.EmployeeRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        Employee employee = new Employee();
        applyRequest(employee, request);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(UUID id, EmployeeRequest request) {
        Employee existing = getEmployeeById(id);
        if (!existing.getEmail().equals(request.email())
                && employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        applyRequest(existing, request);
        return employeeRepository.save(existing);
    }

    private void applyRequest(Employee employee, EmployeeRequest request) {
        employee.setFullName(request.fullName());
        employee.setEmail(request.email());
        employee.setPhoneNumber(request.phoneNumber());
        employee.setDateOfBirth(request.dateOfBirth());
        employee.setHireDate(request.hireDate());
        employee.setDepartment(request.department());
        employee.setSalary(request.salary());
        employee.setStatus(request.status());
    }

    public void deleteEmployee(UUID id) {
        Employee existing = getEmployeeById(id);
        employeeRepository.delete(existing);
    }
}