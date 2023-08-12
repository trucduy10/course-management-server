package com.aptech.coursemanagementserver.dtos;

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
public class SummaryDashboardDto {
    private int totalUser;
    private int todayRegister;
    private double yearRevenue;
    private double monthRevenue;
}
