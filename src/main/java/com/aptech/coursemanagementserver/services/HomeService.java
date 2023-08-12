package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.CategoryEnrollmentDto;
import com.aptech.coursemanagementserver.dtos.RevenueYearDto;
import com.aptech.coursemanagementserver.dtos.SearchDto;
import com.aptech.coursemanagementserver.dtos.SummaryDashboardDto;

public interface HomeService {
    public List<SearchDto> searchAll(String name);

    public SummaryDashboardDto getSummaryDashboard();

    public List<CategoryEnrollmentDto> getCategoryEnrollmentChart();

    public List<RevenueYearDto> getRevenueYearChart(int year);
}
