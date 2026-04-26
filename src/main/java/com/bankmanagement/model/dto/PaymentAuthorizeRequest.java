package com.bankmanagement.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentAuthorizeRequest {
    private Integer paymentId;
    private String secretKey;
}
