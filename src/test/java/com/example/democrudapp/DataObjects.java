package com.example.democrudapp;

import com.example.democrudapp.model.Employee;

public class DataObjects {

    public static Employee createEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Max");
        employee.setLastName("Mustermann");
        employee.setEmail("max.mustermann@mail.com");
        return employee;
    }
}
