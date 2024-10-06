package com.testing.sbtest.service.impl;

import com.testing.sbtest.exception.ResourceNotFoundException;
import com.testing.sbtest.model.Employee;
import com.testing.sbtest.repository.EmployeeRepository;
import com.testing.sbtest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public Employee savedEmployee(Employee employee) {
        Optional<Employee> employeeSaved = this.employeeRepository.findByEmail(employee.getEmail());
        if (employeeSaved.isPresent())
            throw new ResourceNotFoundException("Employee with email " + employee.getEmail() + " already exists");
        return this.employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Employee getEmployeeById(Long id) {
        return this.employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    @Transactional
    @Override
    public Employee updateEmployee(Employee employee) {
        Employee employeeToUpdate = this.employeeRepository.findById(employee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + employee.getId() + " not found"));
        employeeToUpdate.setFirstName(employee.getFirstName());
        employeeToUpdate.setLastName(employee.getLastName());
        employeeToUpdate.setEmail(employee.getEmail());
        return this.employeeRepository.save(employeeToUpdate);
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Optional<Employee> employeeExist = this.employeeRepository.findById(id);
        if (employeeExist.isPresent())
            throw new ResourceNotFoundException("Employee with email " + employeeExist.get().getEmail() + " already exists");
        this.employeeRepository.deleteById(id);
    }
}