package com.aptech.coursemanagementserver.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.aptech.coursemanagementserver.dtos.CategoryEnrollmentDto;

public class CategoryEnrollmentMapper implements RowMapper<CategoryEnrollmentDto> {

    @Override
    @Nullable
    public CategoryEnrollmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEnrollmentDto dto = new CategoryEnrollmentDto();
        dto.setName(rs.getString("name"));
        dto.setPercent(rs.getFloat("percent"));
        return dto;
    }

}
