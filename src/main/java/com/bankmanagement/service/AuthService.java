package com.bankmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankmanagement.model.Customer;
import com.bankmanagement.model.JwtResponse;
import com.bankmanagement.model.LoginRequest;
import com.bankmanagement.repository.CustomerRepository;

@Service
public class AuthService {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private TokenService tokenService;

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        System.out.println("Authenticating user with email: " + loginRequest.getEmail());

        // Validate user credentials
        Customer customer = repository.findByEmail(loginRequest.getEmail());
        if (customer != null && customer.getPassword().equals(loginRequest.getPassword())) {
            // Generate JWT token
            String token = tokenService.generateToken(customer.getId(), customer.getEmail(), customer.getRole());
            
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
