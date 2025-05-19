package com.example.democrudapp.service;

import com.example.democrudapp.model.Employee;
import com.example.democrudapp.model.EmployeeRequest;
import com.example.democrudapp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.getReferenceById(id);
    }

    public Employee createEmployee(String firstName, String lastName) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        return employeeRepository.save(employee);
    }

    public void deleteEmployeeById(long id) {
        employeeRepository.deleteById(id);
        log.debug("Employee with id=[{}] was deleted.", id);
    }

    public Employee updateEmployeeById(long id, EmployeeRequest employeeRequest) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            if (employeeRequest.getFirstName() != null) existingEmployee.setFirstName(employeeRequest.getFirstName());
            if (employeeRequest.getLastName() != null) existingEmployee.setLastName(employeeRequest.getLastName());
            employeeRepository.save(existingEmployee);
            return existingEmployee;
        } else {
            return null;
        }
    }
}
