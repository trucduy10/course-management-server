package com.aptech.coursemanagementserver.dtos;

import java.util.ArrayList;
import java.util.List;

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
public class ExamResultResponseDto {
    private QuestionDto question;

    @Builder.Default
    private List<AnswerDto> answers = new ArrayList<>();

    private int examSession;

    private long courseId;

    private long userId;

    private int limitTime;

    private int totalExamTime;

}
