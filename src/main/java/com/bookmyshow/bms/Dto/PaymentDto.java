package com.bookmyshow.bms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private String transactionId;
    private String status;
    private String paymentMethod;
    private Double amount;
    private LocalDateTime paymentTime;
}
