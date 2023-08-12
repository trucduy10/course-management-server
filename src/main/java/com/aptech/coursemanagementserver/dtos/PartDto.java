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
public class PartDto {
    private long id;

    private long courseId;

    private double maxPoint;

    private int limitTime;
    private String updatedBy;

    @Builder.Default
    private int status = 0;
}
