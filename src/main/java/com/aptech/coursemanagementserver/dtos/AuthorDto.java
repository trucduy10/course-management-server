package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

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
public class AuthorDto {
    private long id;
    private String name;
    private String image;
    private String title;
    private String information;
    private String updatedBy;

    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
}
