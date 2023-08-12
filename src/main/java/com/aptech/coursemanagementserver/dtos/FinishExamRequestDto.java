package com.aptech.coursemanagementserver.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinishExamRequestDto {
    private List<AnswerDetailDto> answers;
    private long courseId;
    private int examSession;
    private int totalExamTime;
    private long userId;
}
