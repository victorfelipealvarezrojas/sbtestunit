package com.testing.sbtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.sbtest.model.Employee;
import com.testing.sbtest.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.given;


@DisplayName("Test Contoller Layer EmployeeController")
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private Employee employee2;

    @BeforeEach
    public void setup() {
        // Given - precondition or setup
        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(1L).
                build();

        employee2 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.cl")
                .id(2L).
                build();
    }

    @DisplayName("Test find employee when success")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given
        given(this.employeeService.savedEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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
        // given
        given(this.employeeService.getAllEmployees()).willReturn(List.of(employee, employee2));
        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(employee))));
        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));
    }

}
