package com.bankmanagement.model.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentInitiateRequest {
    private Integer fromAccountId;
    private Integer toAccountId;
    private BigDecimal amount;
    private String description;
}
