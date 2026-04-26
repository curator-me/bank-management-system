package com.bankmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankmanagement.model.BankTransaction;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Integer> {
    
}
