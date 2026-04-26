package com.bankmanagement.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.bankmanagement.model.Customer;
import com.bankmanagement.service.CustomerService;
import com.bankmanagement.service.TokenService;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("")
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer, @RequestHeader("Authorization") String authHeader) {
        if(!tokenService.authorizeToken(authHeader,"employee")) {
            return ResponseEntity.status(403).body("Forbidden: Only employee can create customers");
        }
        return customerService.createCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {

        if (!tokenService.authorizeToken(authHeader, "employee")) {
            return ResponseEntity.status(403).body("Forbidden: Only employee can delete customers");
        }

        return customerService.deleteCustomer(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }
}