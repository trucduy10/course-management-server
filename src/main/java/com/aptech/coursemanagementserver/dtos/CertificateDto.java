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
public class CertificateDto {
    private String fullName;
    private String courseName;
    private String grade;
    private Date completedDate;
}
