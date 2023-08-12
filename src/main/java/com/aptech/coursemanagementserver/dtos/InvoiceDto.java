package com.aptech.coursemanagementserver.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class InvoiceDto {
    private String transactionId;
    private Date created_at;
    private String userName;
    private String courseName;
    private double price;
    private double net_price;
}
