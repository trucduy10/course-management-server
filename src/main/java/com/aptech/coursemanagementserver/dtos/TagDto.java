package com.aptech.coursemanagementserver.dtos;

import java.time.LocalDateTime;

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
public class TagDto {
    private long id;
    private String name;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
