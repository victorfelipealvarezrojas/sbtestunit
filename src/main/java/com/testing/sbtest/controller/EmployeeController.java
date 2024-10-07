package com.testing.sbtest.controller;

import com.testing.sbtest.model.Employee;
import com.testing.sbtest.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return this.employeeService.savedEmployee(employee);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Employee> getEmployees() {
        return this.employeeService.getAllEmployees();
    }

}
