package com.bankmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankmanagement.model.Employee;
import com.bankmanagement.service.TokenService;
import com.bankmanagement.service.employeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private employeeService employeeService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("")
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer Id) {
        return employeeService.getEmployeeById(Id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee,
            @RequestHeader("Authorization") String authHeader) {

        if (!tokenService.authorizeToken(authHeader, "admin"))
            return ResponseEntity.status(403).body("Unauthorized access");

        return employeeService.createEmployee(employee);
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer Id,
            @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.authorizeToken(authHeader, "admin"))
            return ResponseEntity.status(403).body("Unauthorized access");

        return employeeService.deleteEmployee(Id);
    }
}
