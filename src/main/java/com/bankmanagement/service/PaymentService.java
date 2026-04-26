package com.bankmanagement.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankmanagement.model.Account;
import com.bankmanagement.model.BankTransaction;
import com.bankmanagement.model.Payment;
import com.bankmanagement.model.dto.*;
import com.bankmanagement.repository.AccountRepository;
import com.bankmanagement.repository.BankTransactionRepository;
import com.bankmanagement.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    /**
     * Initiate a new payment.
     * Creates a payment record with status "initiated".
     * Validates both accounts exist, are active, and the sender has sufficient balance.
     */
    public ResponseEntity<?> initiatePayment(PaymentInitiateRequest request, Integer customerId) {
        // Validate request fields
        if (request.getFromAccountId() == null || request.getToAccountId() == null || request.getAmount() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fromAccountId, toAccountId, and amount are required");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero");
        }

        if (request.getFromAccountId().equals(request.getToAccountId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot transfer to the same account");
        }

        // Validate from account
        Optional<Account> fromAccountOpt = accountRepository.findById(request.getFromAccountId());
        if (fromAccountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Source account not found");
        }
        Account fromAccount = fromAccountOpt.get();

        // Verify the account belongs to the authenticated customer
        if (fromAccount.getCustomer().getId() != customerId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own the source account");
        }

        // Check account is active
        if (!"active".equals(fromAccount.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source account is not active");
        }

        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
        }

        // Validate to account
        Optional<Account> toAccountOpt = accountRepository.findById(request.getToAccountId());
        if (toAccountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destination account not found");
        }
        Account toAccount = toAccountOpt.get();

        if (!"active".equals(toAccount.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Destination account is not active");
        }

        // Create payment
        Payment payment = new Payment();
        payment.setFromAccount(fromAccount);
        payment.setToAccount(toAccount);
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setReferenceNumber(generateReferenceNumber());
        payment.setStatus("initiated");

        try {
            Payment savedPayment = paymentRepository.save(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error initiating payment: " + e.getMessage());
        }
    }

    /**
     * Authorize a payment.
     * Transitions payment from "initiated" to "authorized".
     * Requires the source account's secret key for verification.
     */
    public ResponseEntity<?> authorizePayment(PaymentAuthorizeRequest request, Integer customerId) {
        if (request.getPaymentId() == null || request.getSecretKey() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("paymentId and secretKey are required");
        }

        Optional<Payment> paymentOpt = paymentRepository.findById(request.getPaymentId());
        if (paymentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        Payment payment = paymentOpt.get();

        // Verify the payment belongs to the authenticated customer
        if (payment.getFromAccount().getCustomer().getId() != customerId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to authorize this payment");
        }

        // Check payment is in correct state
        if (!"initiated".equals(payment.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Payment cannot be authorized. Current status: " + payment.getStatus());
        }

        // Verify secret key of the source account
        if (!payment.getFromAccount().getSecretKey().equals(request.getSecretKey())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid secret key");
        }

        // Authorize the payment
        payment.setStatus("authorized");
        payment.setAuthorizedBy(customerId);

        try {
            Payment updatedPayment = paymentRepository.save(payment);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error authorizing payment: " + e.getMessage());
        }
    }

    /**
     * Confirm a payment and execute the actual fund transfer.
     * Transitions payment from "authorized" to "confirmed".
     * Debits the source account, credits the destination account,
     * and creates BankTransaction records for both.
     */
    @Transactional
    public ResponseEntity<?> confirmPayment(PaymentConfirmRequest request, Integer customerId) {
        if (request.getPaymentId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("paymentId is required");
        }

        Optional<Payment> paymentOpt = paymentRepository.findById(request.getPaymentId());
        if (paymentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        Payment payment = paymentOpt.get();

        // Verify the payment belongs to the authenticated customer
        if (payment.getFromAccount().getCustomer().getId() != customerId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to confirm this payment");
        }

        // Check payment is in correct state
        if (!"authorized".equals(payment.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Payment cannot be confirmed. Current status: " + payment.getStatus());
        }

        // Re-validate sufficient balance (could have changed since initiation)
        Account fromAccount = payment.getFromAccount();
        if (fromAccount.getBalance().compareTo(payment.getAmount()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance to complete transfer");
        }

        try {
            Account toAccount = payment.getToAccount();

            // Debit source account
            fromAccount.setBalance(fromAccount.getBalance().subtract(payment.getAmount()));
            accountRepository.save(fromAccount);

            // Credit destination account
            toAccount.setBalance(toAccount.getBalance().add(payment.getAmount()));
            accountRepository.save(toAccount);

            // Create withdrawal transaction for source account
            BankTransaction withdrawalTx = new BankTransaction();
            withdrawalTx.setType("withdrawal");
            withdrawalTx.setAmount(payment.getAmount());
            withdrawalTx.setTimestamp(LocalDateTime.now());
            withdrawalTx.setAccount(fromAccount);
            bankTransactionRepository.save(withdrawalTx);

            // Create deposit transaction for destination account
            BankTransaction depositTx = new BankTransaction();
            depositTx.setType("deposit");
            depositTx.setAmount(payment.getAmount());
            depositTx.setTimestamp(LocalDateTime.now());
            depositTx.setAccount(toAccount);
            bankTransactionRepository.save(depositTx);

            // Update payment status
            payment.setStatus("confirmed");
            Payment confirmedPayment = paymentRepository.save(payment);

            return ResponseEntity.status(HttpStatus.OK).body(confirmedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error confirming payment: " + e.getMessage());
        }
    }

    /**
     * Get payment status by ID.
     */
    public ResponseEntity<?> getPaymentStatus(Integer paymentId, Integer customerId) {
        if (paymentId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("paymentId is required");
        }

        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        Payment payment = paymentOpt.get();

        // Verify ownership: the customer must be either the sender or receiver
        int fromCustomerId = payment.getFromAccount().getCustomer().getId();
        int toCustomerId = payment.getToAccount().getCustomer().getId();

        if (fromCustomerId != customerId && toCustomerId != customerId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this payment");
        }

        Map<String, Object> statusResponse = new HashMap<>();
        statusResponse.put("paymentId", payment.getId());
        statusResponse.put("referenceNumber", payment.getReferenceNumber());
        statusResponse.put("status", payment.getStatus());
        statusResponse.put("amount", payment.getAmount());
        statusResponse.put("fromAccountId", payment.getFromAccount().getId());
        statusResponse.put("toAccountId", payment.getToAccount().getId());
        statusResponse.put("description", payment.getDescription());
        statusResponse.put("createdAt", payment.getCreatedAt());
        statusResponse.put("updatedAt", payment.getUpdatedAt());

        return ResponseEntity.status(HttpStatus.OK).body(statusResponse);
    }

    /**
     * Cancel a payment.
     * Only payments in "initiated" or "authorized" state can be cancelled.
     * Confirmed payments cannot be reversed through this endpoint.
     */
    public ResponseEntity<?> cancelPayment(PaymentCancelRequest request, Integer customerId) {
        if (request.getPaymentId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("paymentId is required");
        }

        Optional<Payment> paymentOpt = paymentRepository.findById(request.getPaymentId());
        if (paymentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        Payment payment = paymentOpt.get();

        // Verify ownership
        if (payment.getFromAccount().getCustomer().getId() != customerId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to cancel this payment");
        }

        // Can only cancel if initiated or authorized
        if ("confirmed".equals(payment.getStatus()) || "cancelled".equals(payment.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Payment cannot be cancelled. Current status: " + payment.getStatus());
        }

        payment.setStatus("cancelled");
        if (request.getReason() != null && !request.getReason().isEmpty()) {
            payment.setDescription(payment.getDescription() != null
                    ? payment.getDescription() + " | Cancelled: " + request.getReason()
                    : "Cancelled: " + request.getReason());
        }

        try {
            Payment cancelledPayment = paymentRepository.save(payment);
            return ResponseEntity.status(HttpStatus.OK).body(cancelledPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error cancelling payment: " + e.getMessage());
        }
    }

    /**
     * Generates a unique reference number for each payment.
     */
    private String generateReferenceNumber() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
