package com.aptech.coursemanagementserver.repositories;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.aptech.coursemanagementserver.dtos.CategoryEnrollmentDto;
import com.aptech.coursemanagementserver.dtos.RevenueYearDto;
import com.aptech.coursemanagementserver.dtos.SummaryDashboardDto;
import com.aptech.coursemanagementserver.mappers.CategoryEnrollmentMapper;
import com.aptech.coursemanagementserver.mappers.RevenueYearMapper;

@Component
public class DashBoardRepository {
    private SimpleJdbcCall simpleJdbcCall;
    // private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public DashBoardRepository(JdbcTemplate jdbcTemplate) {

        // this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

    }

    public SummaryDashboardDto getSummaryDashboard() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
        simpleJdbcCall.withProcedureName("sp_summary_dashboard").declareParameters(
                new SqlInOutParameter("total_user", Types.INTEGER),
                new SqlInOutParameter("today_register", Types.INTEGER),
                new SqlInOutParameter("year_revenue", Types.FLOAT),
                new SqlInOutParameter("month_revenue", Types.FLOAT));
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        Map<String, Object> result = simpleJdbcCall.execute(parameterSource);
        SummaryDashboardDto dashboardDto = new SummaryDashboardDto();
        dashboardDto.setTotalUser((int) result.get("total_user"));
        dashboardDto.setTodayRegister((int) result.get("today_register"));
        dashboardDto.setYearRevenue((double) result.get("year_revenue"));
        dashboardDto.setMonthRevenue((double) result.get("month_revenue"));
        return dashboardDto;
    }

    public List<CategoryEnrollmentDto> getCategoryEnrollment() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
        simpleJdbcCall.withProcedureName("sp_get_category_enrollment").returningResultSet("rsGetCategoryEnroll",
                new CategoryEnrollmentMapper());

        Map<String, Object> result = simpleJdbcCall.execute();

        @SuppressWarnings("unchecked")
        List<CategoryEnrollmentDto> dtos = (List<CategoryEnrollmentDto>) result.get("rsGetCategoryEnroll");

        return dtos;
    }

    public List<RevenueYearDto> getRevenueYear(int year) {

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
        simpleJdbcCall.withProcedureName("sp_get_revenue_by_date")
                .declareParameters(new SqlParameter("year", Types.INTEGER))
                .returningResultSet("rsGetRevenueYear",
                        new RevenueYearMapper());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("year", year);
        Map<String, Object> result = simpleJdbcCall.execute(parameterSource);

        @SuppressWarnings("unchecked")
        List<RevenueYearDto> dtos = (List<RevenueYearDto>) result.get("rsGetRevenueYear");

        return dtos;
    }

}
