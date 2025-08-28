package com.example.democrudapp.controller;

import com.example.democrudapp.error.EmployeeNotFoundException;
import com.example.democrudapp.model.Employee;
import com.example.democrudapp.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.democrudapp.DataObjects.createEmployee;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
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

}
