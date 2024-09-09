package com.testing.sbtest.repository;

import com.testing.sbtest.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Test Spring Data JPA EmployeeRepository")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save employee when success")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // Given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(1L).build();

        // When - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test find all employees when success")
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        // Given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(2L).build();

        Employee employee2 = Employee.builder()
                .firstName("John2")
                .lastName("Doe2")
                .email("email2@email.cl")
                .id(3L).build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // When - action or the behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // Then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(4L)
                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        // then - verify the output
        if (optionalEmployee.isPresent()) {
            optionalEmployee.ifPresent(employeeDB -> assertThat(employeeDB).isNotNull());
        }
    }


}
