package com.bankmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankmanagement.model.Account;
import com.bankmanagement.repository.AccountRepository;
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public ResponseEntity<?> getAccountById(Integer customerId, Integer accountId) {
        if (customerId == null || accountId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("customerId or accountId cannot be null");
        }
        try {
            Optional<Account> res = accountRepository.findById(accountId);
            if (res.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
            }

            Account account = res.get();

            if (account.getCustomer() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account!");
            }

            Integer accountCustomerId = account.getCustomer().getId();
            if (!accountCustomerId.equals(customerId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Customer ID does not match account's customer ID");
            }

            return ResponseEntity.status(HttpStatus.OK).body(account);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

    public ResponseEntity<?> createAccount(Account account) {
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account data is required");
        }
        if (account.getType() == null || account.getBalance() == null || account.getCustomer() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(accountRepository.save(account));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating account: " + e.getMessage());
        }
    }
}
