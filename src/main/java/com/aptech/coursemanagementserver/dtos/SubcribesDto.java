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
public class SubcribesDto {
    private long id;
    private long userId;
    private long authorId;
    private String authorName;
    private String image;
    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
}
