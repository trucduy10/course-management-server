-- DECLARE @StartDate AS date;
-- DECLARE @EndDate AS date;
-- DECLARE @DaysBetween AS int;
-- SELECT @StartDate   = '01/01/2022',
--        @EndDate     = '12/31/2023',
--        @DaysBetween = (1+DATEDIFF(DAY, @StartDate, @EndDate));
-- INSERT orders ([created_at], [description], [duration], [image], [name], [net_price], [payment], [price], [status], 
-- [transaction_id], [updated_at], [user_description], [course_id], [user_id])
-- SELECT DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate) , c.description, c.duration, c.image, c.name, c.net_price
-- , 'PAYPAL', c.price, 'COMPLETED', null, GETUTCDATE(), null, c.id, u.id  
-- FROM course c CROSS JOIN users u
-- WHERE u.role ='USER'

-- Insert order theo enrollment đã tạo và random Payment MOMO OR PAYPAL
DECLARE @StartDate AS date;
DECLARE @EndDate AS date;
DECLARE @DaysBetween AS int;
DECLARE @Time time = '00:00:00.0000000';
SELECT @StartDate   = '01/01/2022',
       @EndDate     = GETUTCDATE(),
       @DaysBetween = (1+DATEDIFF(DAY, @StartDate, @EndDate));
INSERT orders ([created_at], [description], [duration], [image], [name], [net_price], [payment], [price], [status], 
[transaction_id], [updated_at], [user_description], [course_id], [user_id])

SELECT  CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)) AS created_at , c.description,
c.duration, c.image, c.name, c.net_price, b.provider
, c.price, 'COMPLETED' AS status, NEWID() AS tranid, GETUTCDATE() update_at, u.name AS 'DES' , c.id AS course_id, u.id  
FROM 
course c INNER JOIN enrollment e ON c.id = e.course_id
INNER JOIN users u ON e.user_id = u.id AND u.role ='USER'
CROSS APPLY(SELECT TOP 1 * FROM
(
SELECT 'PAYPAL' AS provider
UNION
SELECT 'MOMO'
) AS a ORDER BY NEWID(), CHECKSUM(provider,e.course_id, e.user_id) ) AS b

WHERE u.role ='USER'





SELECT payment, count(*) FROM orders GROUP BY payment