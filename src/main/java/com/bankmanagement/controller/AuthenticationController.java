package com.bankmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankmanagement.model.LoginRequest;
import com.bankmanagement.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Received login request for email: " + loginRequest.getEmail());
        return authService.login(loginRequest);
    }
}




/*
 * Note on import organization:
 * 1. Java standard library (java.*)
 * 2. Third-party libraries (org.*, jakarta.*, etc.)
 * 3. Project-specific packages (com.yourproject.*)
 * use Shift + Ctrl + O in vs-code to optimize imports and remove unused ones.
 */