package com.bankmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankmanagement.model.Employee;
import com.bankmanagement.service.employeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    
    @Autowired
    private employeeService employeeService;

    @GetMapping("")
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}") 
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer Id) {
        return employeeService.getEmployeeById(Id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer Id) {
        return employeeService.deleteEmployee(Id);
    }
}
