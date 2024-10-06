package com.testing.sbtest.service;

import com.testing.sbtest.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee savedEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Employee employee);

    void deleteEmployee(Long id);
}