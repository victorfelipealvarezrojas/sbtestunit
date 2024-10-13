package com.testing.sbtest.controller;

import com.testing.sbtest.model.Employee;
import com.testing.sbtest.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Employee> getEmployeesById() {
        return this.employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(name = "id") Long employeeId) {
        return this.employeeService.getEmployeeById(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(name = "id") Long employeeId,
                                                   @RequestBody Employee employee) {
        return this.employeeService.getEmployeeById(employeeId)
                .map(employeeObj -> {
                    employee.setId(employeeObj.getId());
                    return ResponseEntity.ok(this.employeeService.savedEmployee(employee));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(name = "id") Long employeeId) {
        return this.employeeService.getEmployeeById(employeeId)
                .map(employee -> {
                    this.employeeService.deleteEmployee(employee.getId());
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}