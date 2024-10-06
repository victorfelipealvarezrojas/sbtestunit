package com.testing.sbtest.service;

import com.testing.sbtest.exception.ResourceNotFoundException;
import com.testing.sbtest.model.Employee;
import com.testing.sbtest.repository.EmployeeRepository;
import com.testing.sbtest.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test Spring Service Layer EmployeeService")
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private Employee employee2;

    @BeforeEach
    public void setup(){
        // employeeRepository = Mockito.mock(EmployeeRepository.class);
        // employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(1L)
                .build();

        employee2 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(2L)
                .build();
    }

    @Test
    @DisplayName("Test find employee when success")
    public void givenEmployeesList_whenGetAllEmployees_thenGetEmployeesList() {
        // given
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));
        // then
        List<Employee> employees = employeeService.getAllEmployees();
        // Then
        assertThat(employees).hasSize(2);
    }

    @Test
    @DisplayName("Test find all employees when no data")
    public void givenNoEmployee_whenFindAll_thenReturnEmptyList() {
        // Given - precondition or setup
        // given
        given(employeeRepository.findAll()).willReturn(List.of());
        // When - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();
        // Then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList).isEqualTo(List.of());
    }

    @Test
    @DisplayName("Test save employee when success")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployeeObject() {
        // given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        // when
        Employee saveEmployee = employeeService.savedEmployee(employee);
        // then
        assertThat(employeeService.savedEmployee(employee)).isEqualTo(saveEmployee);
    }

    @Test
    @DisplayName("Test save employee when throws exception")
    public void givenEmployeeObject_whenSaveEmployee_thenThrowRException() {
        // given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        // when
        assertThrows(ResourceNotFoundException.class, () -> employeeService.savedEmployee(employee));
        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Test get employee by id when success")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        // when
        Employee employeeFound = employeeService.getEmployeeById(employee.getId());
        // then
        assertThat(employeeFound).isEqualTo(employee);
    }

    @Test
    @DisplayName("Test update employee when success")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        // given
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        // when
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        // then
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");
    }

    //  willDoNothing configurar el comportamiento de un método void
    @Test
    @DisplayName("Test delete employee when success")
    public void givenEmployeeId_whenDeleteEmployee_thenVerifyDeleteMethod() {
        long employeeId = 1L;
        // given
        willDoNothing().given(employeeRepository).deleteById(employeeId);
        // when
        employeeService.deleteEmployee(employeeId);
        // then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}