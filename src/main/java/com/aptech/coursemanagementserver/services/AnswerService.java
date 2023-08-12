
package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.AnswerDto;
import com.aptech.coursemanagementserver.models.Answer;

public interface AnswerService {
    public AnswerDto findById(long id);

    public List<AnswerDto> findAllByQuestionId(long questionId);

    public void save(AnswerDto answerDto);

    public void deleteAnswer(long id);

    public AnswerDto toDto(Answer answer);
}
