package com.example.democrudapp.controller;

import com.example.democrudapp.error.EmployeeNotFoundException;
import com.example.democrudapp.model.Employee;
import com.example.democrudapp.model.EmployeeMapper;
import com.example.democrudapp.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.democrudapp.DataObjects.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_whenCalled_returnsEmployee() throws Exception {
        Employee employee = createEmployee();
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Max"))
                .andExpect(jsonPath("$.lastName").value("Mustermann"))
                .andExpect(jsonPath("$.email").value("max.mustermann@mail.com"));
    }


    @Test
    void getEmployeeById_whenSearchForNonExistentEmployee_returnsNotFound() throws Exception {
        final String errorMessage = "Employee with ID 99 not found";
        when(employeeService.getEmployeeById(99L)).thenThrow(new EmployeeNotFoundException(errorMessage));
        mockMvc.perform(get("/api/employee/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void getEmployeeById_whenInvalidIdGiven_returnsBadRequest() throws Exception {
        final String errorMessage = "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"XYZ\"";
        mockMvc.perform(get("/api/employee/XYZ"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void getEmployees_whenCalled_returnsEmployeeList() throws Exception {
        when(employeeService.findAll()).thenReturn(createEmployeeList());
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createEmployeeList())));
    }

    @Test
    void createEmployee_whenCalled_returnsEmployee() throws Exception {
        when(employeeService.createEmployee("Max", "Mustermann", "max.mustermann@mail.com")).thenReturn(createEmployee());
        mockMvc.perform(put("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployee())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createEmployee())));
    }

    @Test
    void createEmployee_whenEmailInvalid_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeWithInvalidEmail())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email: Email should be valid"))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void createEmployee_whenFirstnameEmpty_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeWithEmptyFirstname())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("firstName: First name is required"))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void createEmployee_whenLastnameEmpty_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeWithEmptyLastname())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("lastName: Last name is required"))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void deleteEmployee_whenCalled_returnsEmptyResponseBody() throws Exception {
        doNothing().when(employeeService).deleteEmployeeById(1L);
        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // body is empty
    }

    @Test
    void deleteEmployee_whenSearchForNonExistentEmployee_returnsNotFound() throws Exception {
        final String errorMessage = "Employee with ID 99 not found";
        doThrow(new EmployeeNotFoundException(errorMessage)).when(employeeService).deleteEmployeeById(99L);
        mockMvc.perform(delete("/api/employee/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void deleteEmployee_whenInvalidIdGiven_returnsBadRequest() throws Exception {
        final String errorMessage = "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"XYZ\"";
        mockMvc.perform(delete("/api/employee/XYZ"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void updateEmployee_whenCalled_returnsEmployee() throws Exception {
        when(employeeService.updateEmployeeById(1L, EmployeeMapper.toEmployeeRequest(createEmployee()))).thenReturn(createEmployee());
        mockMvc.perform(put("/api/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployee())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createEmployee())));
    }

    @Test
    void updateEmployee_whenSearchForNonExistentEmployee_returnsNotFound() throws Exception {
        final String errorMessage = "Employee with ID 99 not found";
        when(employeeService.updateEmployeeById(99L, EmployeeMapper.toEmployeeRequest(createEmployee()))).thenThrow(new EmployeeNotFoundException(errorMessage));
        mockMvc.perform(put("/api/employee/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployee())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

    @Test
    void updateEmployee_whenInvalidIdGiven_returnsBadRequest() throws Exception {
        final String errorMessage = "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"XYZ\"";
        mockMvc.perform(put("/api/employee/XYZ"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.supportCode").isNotEmpty());
    }

}
