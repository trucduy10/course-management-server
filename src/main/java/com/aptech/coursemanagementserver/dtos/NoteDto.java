package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto extends LessonTrackingDto {
    private String description;

    @CreationTimestamp
    private Instant created_at = Instant.now();
}
