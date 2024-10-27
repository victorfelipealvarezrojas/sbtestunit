package com.testing.sbtest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.sbtest.model.Employee;
import com.testing.sbtest.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Integration Test Contoller Layer EmployeeController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// @Testcontainers
public class EmployeeControllerICTest extends AbstractContainerBaseTest {

    /**
    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");
            //.withDatabaseName("integration-tests-db")
            //.withUsername("sa")
            //.withPassword("sa");

    @DynamicPropertySource
    public  static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }*/

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private Employee employee2;

    @BeforeEach
    void setup() {
        // Given - precondition or setup
        employee = Employee.builder()
                .firstName("John pedro")
                .lastName("Doe")
                .email("pedro@email.cl")
                .id(1L)
                .build();

        employee2 = Employee.builder()
                .firstName("John juan")
                .lastName("Doe")
                .email("juan@email.cl")
                .id(2L)
                .build();

        employeeRepository.deleteAll();
    }

    @DisplayName("Test find employee when success")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(employee))));

        // then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("Test find all employees when success")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnAllEmployees() throws Exception {

        employeeRepository.saveAll(List.of(employee, employee2));

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(employee))));
        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));
    }

    @Test
    @DisplayName("Test find employee by id when success")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{

        Employee emp = employeeRepository.save(employee);

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", emp.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    @DisplayName("Test find employee by id when employee not found")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/2")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Test update employee by id when success")
    public void givenEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        Employee emp = employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", emp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(employee2))));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee2.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee2.getLastName())))
                .andExpect(jsonPath("$.email", is(employee2.getEmail())));
    }

    @Test
    @DisplayName("Test update employee by id when employee not found")
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturn404() throws Exception {

        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        // when
        ResultActions response = mockMvc.perform(put("/api/employees/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(employee2))));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Test delete employee by id when success")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnSuccess() throws Exception {

        Employee savedEmployee =  Employee.builder()
                .firstName("John Diego")
                .lastName("Doe")
                .email("diego@email.cl")
                .id(3L)
                .build();
        Employee emp = employeeRepository.save(savedEmployee);

        // when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", emp.getId()));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }



}
