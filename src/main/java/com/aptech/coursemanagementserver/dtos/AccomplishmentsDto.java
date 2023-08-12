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
public class AccomplishmentsDto {
    private long courseId;
    private String courseName;
    private String courseImage;
    private String courseSlug;
    private String categoryName;
    private String categorySlug;
    private int courseDuration;
    private double courseRating;
    private int courseTotalEnroll;
    private GradeType grade;
    private String certificateUID;
    private int examSession;
    @CreationTimestamp
    private Instant created_at;
}
