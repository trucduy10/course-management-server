package com.aptech.coursemanagementserver.dtos;

import java.time.LocalDateTime;

import com.aptech.coursemanagementserver.enums.AntType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private LocalDateTime timestamp;
    private AntType type;
    private String message;
    private String details;
    private String statusCode;

}
