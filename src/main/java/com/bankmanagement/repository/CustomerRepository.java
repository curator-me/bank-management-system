package com.bankmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankmanagement.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);
    Boolean existsByEmail(String email);
}
