package com.bankmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankmanagement.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByCustomerId(Integer customerId);
}
