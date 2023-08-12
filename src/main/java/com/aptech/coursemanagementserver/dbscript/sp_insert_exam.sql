--2. sp_insert_exam
CREATE OR ALTER PROCEDURE [dbo].[sp_insert_exam] @course_id BIGINT = 0,
@user_id BIGINT = 0, @part_id BIGINT = 0, @session INT OUTPUT
AS
SET NOCOUNT ON;
SET @session = isnull(
(SELECT max(exam_session) + 1 FROM exam_result WHERE user_id = @user_id AND
course_id = @course_id)
,0)
INSERT exam_result( [course_id], [anwser_description], [is_correct],
[question_description],
[question_point], [answer_id], [part_id], [question_id], [user_id],
exam_session)
SELECT 1, a.description, a.is_correct, q.description, q.point, a.id,
q.part_id, q.id, 4,
CASE WHEN @session = 0 THEN 1 ELSE @session END
FROM question q INNER JOIN answer a
ON q.id = a.question_id
WHERE part_id = 1 ;
SET @session = (CASE WHEN @session = 0 THEN 1 ELSE @session end);

DECLARE @session INT
EXEC sp_insert_exam 1, 4, 1, @session OUTPUT
PRINT @session;