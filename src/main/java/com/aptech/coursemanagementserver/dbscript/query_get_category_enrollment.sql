-- Thống kê (%) category đc user mua nhiều nhất
ALTER   PROC [dbo].[sp_get_category_enrollment]  
AS
BEGIN
DECLARE @total_enroll FLOAT
SELECT @total_enroll = COUNT(*) FROM enrollment e1 
INNER JOIN users u ON e1.user_id = u.id
AND u.role = 'USER'
IF(@total_enroll IS NULL OR @total_enroll =0) 
SET @total_enroll = 1;

SELECT ROUND(COUNT(e.course_id) * 100/ @total_enroll, 2) [percent], cat.name 
FROM category cat 
LEFT JOIN   (
course c INNER JOIN 
enrollment e on c.id = e.course_id
INNER JOIN users u ON e.user_id = u.id AND u.role = 'USER') ON cat.id = c.category_id
--WHERE u.role = 'USER'
GROUP BY cat.name
END