package com.bankmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BankTransaction")
@Data
@NoArgsConstructor
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}