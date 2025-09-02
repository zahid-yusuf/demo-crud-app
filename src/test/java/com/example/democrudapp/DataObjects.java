package com.example.democrudapp;

import com.example.democrudapp.model.Employee;

import java.util.List;

public class DataObjects {

    public static Employee createEmployee() {
        return Employee.builder().id(1L).firstName("Max").lastName("Mustermann").email("max.mustermann@mail.com").build();
    }

    public static Employee createEmployeeWithEmptyFirstname() {
        return Employee.builder().id(1L).firstName("").lastName("Mustermann").email("max.mustermann@mail.com").build();
    }

    public static Employee createEmployeeWithEmptyLastname() {
        return Employee.builder().id(1L).firstName("Max").lastName("").email("max.mustermann@mail.com").build();
    }

    public static Employee createEmployeeWithInvalidEmail() {
        return Employee.builder().id(1L).firstName("Max").lastName("Mustermann").email("not-an-valid-email").build();
    }

    public static List<Employee> createEmployeeList() {
        return List.of(
                Employee.builder().id(1L).firstName("Max").lastName("Mustermann").email("max.mustermann@mail.com").build(),
                Employee.builder().id(2L).firstName("Erika").lastName("Mustermann").email("erika.mustermann@mail.com").build()
        );
    }
}
