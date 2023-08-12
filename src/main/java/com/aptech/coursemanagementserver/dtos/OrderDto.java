package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.aptech.coursemanagementserver.enums.OrderStatus;
import com.aptech.coursemanagementserver.enums.payment.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDto {
    private long id;
    private String userName;
    private String courseName;
    private String description;
    private String transactionId;
    private String userDescription;
    private String slug;
    private int duration;
    private String image;
    private double price;
    private double net_price;
    @Builder.Default
    private PaymentType payment = PaymentType.PAYPAL;
    private OrderStatus status;

    @CreationTimestamp
    private Instant created_at;
}
