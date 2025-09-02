package com.example.democrudapp.model;

public class EmployeeMapper {

    public static EmployeeRequest toEmployeeRequest(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName(employee.getFirstName());
        employeeRequest.setLastName(employee.getLastName());
        employeeRequest.setEmail(employee.getEmail());
        return employeeRequest;
    }
}
