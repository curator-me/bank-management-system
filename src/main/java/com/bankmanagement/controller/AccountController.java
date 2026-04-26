package com.bankmanagement.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.bankmanagement.service.AccountService;
import com.bankmanagement.service.TokenService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("")
    public List<?> getAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Integer accountId, @RequestHeader("Authorization") String authHeader) {
        // Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        // remove "Bearer "
        String token = authHeader.substring(7);

        // validate token
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }

        // get customer id from token
        Integer customerId = tokenService.getIdFromToken(token);
        return accountService.getAccountById(customerId, accountId);
    }
}
