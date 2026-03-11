package com.bankmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankmanagement.model.Employee;
import com.bankmanagement.repository.emplopyeeRepository;

@Service
public class employeeService {

    @Autowired
    private emplopyeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public ResponseEntity<?> getEmployeeById(Integer id) {
        if (id == null) {
            return ResponseEntity.status(400).body("ID is required");
        }
        Optional<Employee> res = employeeRepository.findById(id);

        if (res.isEmpty()) {
            return ResponseEntity.status(404).body("Employee not found");
        }

        Employee employee = res.get();
        return ResponseEntity.status(200).body(employee);
    }

    public ResponseEntity<?> createEmployee(Employee employee) {
        if (employee == null) {
            return ResponseEntity.status(400).body("Employee data is required");
        }
        if (employee.getName() == null || employee.getPhoneNumber() == null || employee.getEmail() == null
                || employee.getCity() == null || employee.getStreet() == null || employee.getHouseNo() == null
                || employee.getPassword() == null) {
            return ResponseEntity.status(400).body("Missing required fields");
        }
        try {
            Employee savedEmployee = employeeRepository.save(employee);
            return ResponseEntity.status(201).body(savedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating employee: " + e.getMessage());
        }
    }

    public ResponseEntity<?> deleteEmployee(Integer Id) {
        if(Id == null) {
            return ResponseEntity.status(400).body("Id is required");
        }
        Optional<Employee> res = employeeRepository.findById(Id);

        if(res.isEmpty()) {
            return ResponseEntity.status(404).body("Employee not found");
        }

        try {
            employeeRepository.deleteById(Id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting employee: " + e.getMessage());
        }
        return ResponseEntity.status(200).body("Employee deleted successfully");
    }
}
