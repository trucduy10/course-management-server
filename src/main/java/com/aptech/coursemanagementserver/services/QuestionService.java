package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.QuestionDto;
import com.aptech.coursemanagementserver.models.Question;

public interface QuestionService {
    public QuestionDto findById(long id);

    public List<QuestionDto> findAllByPartId(long partId);

    public void save(QuestionDto questionDto);

    public void deleteQuestion(long id);

    public QuestionDto toDto(Question question);
}
