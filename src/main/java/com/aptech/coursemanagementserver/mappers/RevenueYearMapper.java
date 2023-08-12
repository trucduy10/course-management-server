package com.aptech.coursemanagementserver.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.aptech.coursemanagementserver.dtos.RevenueYearDto;

public class RevenueYearMapper implements RowMapper<RevenueYearDto> {

    @Override
    @Nullable
    public RevenueYearDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        RevenueYearDto dto = new RevenueYearDto();
        dto.setMonth(rs.getInt("month"));
        dto.setYear(rs.getInt("year"));
        dto.setRevenue(rs.getFloat("revenue"));

        return dto;
    }

}
