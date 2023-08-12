package com.aptech.coursemanagementserver.dtos.payment;

import com.aptech.coursemanagementserver.enums.payment.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CheckoutDto {
    private long amount = 1;
    private long userId;
    private long courseId;
    private PaymentType paymentType = PaymentType.PAYPAL;
    private String userDescription;
}
