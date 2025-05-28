package com.example.democrudapp.service;

import com.example.democrudapp.error.EmployeeNotFoundException;
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
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
    }

    public Employee createEmployee(String firstName, String lastName, String email) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Created employee with ID {}", savedEmployee.getId());
        return savedEmployee;
    }

    public void deleteEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
        employeeRepository.delete(employee);
        log.info("Deleted employee with ID {}", id);
    }

    public Employee updateEmployeeById(long id, EmployeeRequest employeeRequest) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            existingEmployee.setFirstName(employeeRequest.getFirstName());
            existingEmployee.setLastName(employeeRequest.getLastName());
            existingEmployee.setEmail(employeeRequest.getEmail());
            log.info("Updated employee with ID {}", id);
            return employeeRepository.save(existingEmployee);
        } else {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found");
        }
    }
}
