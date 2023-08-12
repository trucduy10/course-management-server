package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query(value = """
            SELECT a.* FROM answer a WHERE a.question_id = :questionId
            ORDER BY created_at DESC
            """, nativeQuery = true)
    List<Answer> findByQuestionId(long questionId);
}
