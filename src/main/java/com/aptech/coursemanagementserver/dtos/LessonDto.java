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
public class LessonDto {
    private long id;
    private String name;
    private String description;
    private int duration;
    private long sectionId;
    private String updatedBy;
    @Builder.Default
    private int status = 0;
    @Builder.Default
    private int ordered = 0;
    private boolean isCompleted;
}
