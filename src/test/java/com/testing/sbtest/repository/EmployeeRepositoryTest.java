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

    private Employee employee;

    @BeforeEach
    public void setup() {
        // Given - precondition or setup
        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(1L).
                build();
    }

    @BeforeEach
    public void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save employee when success")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // Given - precondition or setup
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
        Employee employee2 = Employee.builder()
                .firstName("John2")
                .lastName("Doe2")
                .email("email2@email.cl")
                .id(2L).build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // When - action or the behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();
        // Then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("JUnit test for get employee by id operation")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going test
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
        // then - verify the output
        if (optionalEmployee.isPresent()) {
            optionalEmployee.ifPresent(employeeDB -> assertThat(employeeDB).isNotNull());
        }
    }

    @Test
    @DisplayName("JUnit test for get employee by Email")
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going test
        Employee optionalEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        // then - verify the output
        assertThat(optionalEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get employee update")
    public void givenEmployeeRegister_Is_Update() {
        // given - precondition or setup
        Employee e = employeeRepository.save(employee);
        Employee saveEmployee = employeeRepository.findById(e.getId()).get();
        assertThat(saveEmployee.getFirstName()).isEqualTo("John");
        // when - action or the behaviour that we are going test
        saveEmployee.setFirstName("edit");
        employeeRepository.save(saveEmployee);
        Employee updateEmployeeUpdate = employeeRepository.findById(saveEmployee.getId()).get();
        // then - verify the output
        assertThat(updateEmployeeUpdate.getFirstName()).isEqualTo("edit");
    }

    @Test
    @DisplayName("Junit Test employee delete")
    public void givenEmployeeRegister_Is_Delete() {
        // given - precondition or setup
        employeeRepository.save(employee);
        //assertThat(employeeRepository.findById(employee.getId())).isNotEmpty();
        // when - action or the behaviour that we are going test
        employeeRepository.deleteById(employee.getId());
        // then - verify the output
        assertThat(employeeRepository.findById(employee.getId())).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for get employee by JPQL query")
    public void givenEmployeeObject_whenFindByJPQLQuery_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going test
        Employee optionalEmployee = employeeRepository.findByJPQLQuery(employee.getFirstName(), employee.getLastName());
        // then - verify the output
        assertThat(optionalEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get employee by JPQL query with named parameters")
    public void givenEmployeeObject_whenFindByJPQLQueryNamedParams_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going test
        Employee optionalEmployee = employeeRepository.findByJPQLQueryNamedParams(employee.getFirstName(), employee.getLastName());
        // then - verify the output
        assertThat(optionalEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get employee by Native query")
    public void givenEmployeeObject_whenFindByNativeQuery_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going test
        Employee optionalEmployee = employeeRepository.findByNativeQuery(employee.getFirstName(), employee.getLastName());
        // then - verify the output
        assertThat(optionalEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get employee by Native query with named parameters")
    public void givenEmployeeObject_whenFindByNativeQueryNamedParams_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going test
        Employee optionalEmployee = employeeRepository.findByNativeQueryNamedParams(employee.getFirstName(), employee.getLastName());
        // then - verify the output
        assertThat(optionalEmployee).isNotNull();
    }
}