package com.aptech.coursemanagementserver.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LearningDto {
    private List<SectionDto> sectionDto = new ArrayList<>();
    private List<LessonDto> lessonDto = new ArrayList<>();
    private List<VideoDto> videoDto = new ArrayList<>();
}
