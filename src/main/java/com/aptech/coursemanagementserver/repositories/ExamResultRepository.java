package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.aptech.coursemanagementserver.models.ExamResult;

import jakarta.transaction.Transactional;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
        List<ExamResult> findByPartId(long partId);

        List<ExamResult> findByQuestionId(long questionId);

        List<ExamResult> findByAnswerId(long answerId);

        // @Procedure(value = """
        // CREATE OR ALTER PROCEDURE [dbo].[sp_insert_exam] @course_id BIGINT = 0,
        // @user_id BIGINT = 0, @part_id BIGINT = 0, @session INT OUTPUT
        // AS
        // SET NOCOUNT ON;
        // SET @session = isnull(
        // (SELECT max(exam_session) + 1 FROM exam_result WHERE user_id = @user_id AND
        // course_id = @course_id)
        // ,0)
        // INSERT exam_result( [course_id], [anwser_description], [is_correct],
        // [question_description],
        // [question_point], [answer_id], [part_id], [question_id], [user_id],
        // exam_session)
        // SELECT 1, a.description, a.is_correct, q.description, q.point, a.id,
        // q.part_id, q.id, 4,
        // CASE WHEN @session = 0 THEN 1 ELSE @session END
        // FROM question q INNER JOIN answer a
        // ON q.id = a.question_id
        // WHERE part_id = 1 ;
        // SET @session = (CASE WHEN @session = 0 THEN 1 ELSE @session end);

        // DECLARE @session INT
        // EXEC sp_insert_exam 1, 4, 1, @session OUTPUT
        // PRINT @session;
        // """)
        @Procedure(value = """
                        sp_insert_exam
                                        """)
        int createExamResultByPartIdAndUserIdAndCourseId(long part_id, long user_id, long course_id);

        List<ExamResult> findExamResultByCourseIdAndUserIdAndExamSession(long courseId, long userId, int examSession);

        @Query(value = """
                        SELECT e.* FROM exam_result e
                        WHERE exam_session
                        = ( SELECT MAX(exam_session)
                        FROM exam_result
                        WHERE course_id = :courseId
                        AND user_id = :userId )
                        AND course_id = :courseId AND user_id = :userId
                        ORDER BY created_at DESC
                                        """, nativeQuery = true)
        List<ExamResult> findExamResultByCourseIdAndUserId(long courseId, long userId);

        @Query(value = """
                        SELECT  e.* from exam_result e
                        WHERE e.user_id = :userId
                        AND e.certificateuid IS NOT NULL
                        ORDER BY created_at DESC
                                        """, nativeQuery = true)
        List<ExamResult> findPassedExamResultByUserId(long userId);

        @Transactional
        @Modifying
        @Query(value = """
                        UPDATE exam_result
                        SET total_point =
                        (SELECT SUM(question_point) total_point FROM exam_result
                        WHERE user_id = :userId
                        AND course_id = :courseId
                        AND user_answer_id = answer_id
                        AND is_correct = 1)
                        AND exam_session = :examSession
                                        """, nativeQuery = true)
        void updateExamResultByCourseIdAndUserIdAndExamSession(long courseId, long userId, int examSession);
}
