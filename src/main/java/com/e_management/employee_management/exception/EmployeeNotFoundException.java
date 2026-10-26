package com.e_management.employee_management.exception;

import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.UUID;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(UUID id) {
        super("Employee not found with id: " + id);
    }
}



