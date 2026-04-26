package com.bankmanagement.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentCancelRequest {
    private Integer paymentId;
    private String reason;
}
