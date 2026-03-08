package com.bankmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankmanagement.model.Customer;
import com.bankmanagement.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository userRepository;

    public List<Customer> getAllCustomers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> getCustomerById(Integer id) {
        if (id == null) {
            // return new ResponseEntity<>("ID is required", HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is required");
        }
        Optional<Customer> res = userRepository.findById(id); // store result in a variable

        if (res.isEmpty()) { // check if user exists
            // return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Customer user = res.get(); // extract Customer from res

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<?> createCustomer(Customer customer) {
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer data is required");
        }
        if (customer.getName() == null || customer.getPhoneNumber() == null || customer.getEmail() == null
                || customer.getCity() == null || customer.getStreet() == null || customer.getHouseNo() == null
                || customer.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(customer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating customer: " + e.getMessage());
        }
    }

    public ResponseEntity<?> deleteCustomer(Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is required");
        }
        Optional<Customer> res = userRepository.findById(id); // store result in a variable

        if (res.isEmpty()) { // check if user exists
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        try {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Customer deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting customer: " + e.getMessage());
        }
    }

    public ResponseEntity<?> updateCustomer(Integer id, Customer customer) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is required");
        }
        Optional<Customer> res = userRepository.findById(id); // store result in a variable

        if (res.isEmpty()) { // check if user exists
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Customer existingCustomer = res.get(); // extract Customer from res

        // Update fields if they are provided in the request
        if (customer.getName() != null) existingCustomer.setName(customer.getName());
        if (customer.getCity() != null) existingCustomer.setCity(customer.getCity());
        if (customer.getStreet() != null) existingCustomer.setStreet(customer.getStreet());
        if (customer.getHouseNo() != null) existingCustomer.setHouseNo(customer.getHouseNo());
        if (customer.getDateOfBirth() != null) existingCustomer.setDateOfBirth(customer.getDateOfBirth());

        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(existingCustomer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating customer: " + e.getMessage());
        }
    }
}

