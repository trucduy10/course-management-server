package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.aptech.coursemanagementserver.enums.GradeType;

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
public class RetakeExamDto {
    private int examSession;
    private String correctAnswer;
    private int totalExamTime;
    private double totalPoint;
    private GradeType grade;
    private boolean isPassed;
    @CreationTimestamp
    private Instant created_at;
}
