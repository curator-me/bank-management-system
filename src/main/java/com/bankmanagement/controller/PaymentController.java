package com.bankmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.bankmanagement.model.dto.PaymentAuthorizeRequest;
import com.bankmanagement.model.dto.PaymentCancelRequest;
import com.bankmanagement.model.dto.PaymentConfirmRequest;
import com.bankmanagement.model.dto.PaymentInitiateRequest;
import com.bankmanagement.service.PaymentService;
import com.bankmanagement.service.TokenService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TokenService tokenService;

    /**
     * POST /payments/initiate
     * Initiate a new payment between two accounts.
     */
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentInitiateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }
        Integer customerId = tokenService.getIdFromToken(token);
        return paymentService.initiatePayment(request, customerId);
    }

    /**
     * POST /payments/authorize
     * Authorize a previously initiated payment using the account's secret key.
     */
    @PostMapping("/authorize")
    public ResponseEntity<?> authorizePayment(@RequestBody PaymentAuthorizeRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }
        Integer customerId = tokenService.getIdFromToken(token);
        return paymentService.authorizePayment(request, customerId);
    }

    /**
     * POST /payments/confirm
     * Confirm an authorized payment and execute the fund transfer.
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }
        Integer customerId = tokenService.getIdFromToken(token);
        return paymentService.confirmPayment(request, customerId);
    }

    /**
     * GET /payments/{id}/status
     * Get the current status of a payment.
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }
        Integer customerId = tokenService.getIdFromToken(token);
        return paymentService.getPaymentStatus(id, customerId);
    }

    /**
     * POST /payments/cancel
     * Cancel an initiated or authorized payment.
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody PaymentCancelRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }
        Integer customerId = tokenService.getIdFromToken(token);
        return paymentService.cancelPayment(request, customerId);
    }
}
