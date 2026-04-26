package com.bankmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankmanagement.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByReferenceNumber(String referenceNumber);
    List<Payment> findByFromAccountId(Integer accountId);
    List<Payment> findByToAccountId(Integer accountId);
    List<Payment> findByStatus(String status);
}
