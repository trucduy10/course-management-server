--Tổng doanh thu 
GO
CREATE OR ALTER PROC sp_get_revenue_by_date @year INT = 0
AS
BEGIN

SELECT  MONTH(CONVERT(date, DATEADD(hour, 7, o.created_at))) month,
YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at))) year, SUM(o.net_price) revenue FROM orders o 
INNER JOIN users u ON user_id = u.id
WHERE o.status = 'COMPLETED' AND u.role = 'USER'
AND YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at))) = 
case when @year = 0 then YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at))) else @year end
GROUP BY MONTH(CONVERT(date, DATEADD(hour, 7, o.created_at))),YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at)))
ORDER BY YEAR, MONTH
END

EXEC sp_get_revenue_by_date
EXEC sp_get_revenue_by_date 2022
EXEC sp_get_revenue_by_date 2023