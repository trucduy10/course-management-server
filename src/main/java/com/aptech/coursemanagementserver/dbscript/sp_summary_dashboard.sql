--1. sp_summary_dashboard
CREATE OR ALTER PROC sp_summary_dashboard 
@total_user INTEGER OUTPUT, @today_register INTEGER OUTPUT,
@year_revenue FLOAT OUTPUT, @month_revenue FLOAT OUTPUT
AS
BEGIN
--Tổng USER
SELECT @total_user = COUNT(*) FROM users WHERE role = 'USER'
--Tổng USER đăng ký hôm nay
SELECT @today_register = COUNT(*)
FROM users
WHERE CONVERT(date, DATEADD(hour, 7, created_at)) = CONVERT(date, GETDATE())
--Tổng doanh thu năm này
SELECT @year_revenue = ISNULL(SUM(o.net_price),0)
FROM orders o
INNER JOIN users u ON o.user_id = u.id
WHERE YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at))) = YEAR(CONVERT(date, GETDATE()))
AND o.status = 3 AND u.role = 'USER'
--Tổng doanh thu tháng này
SELECT @month_revenue = ISNULL(SUM(o.net_price),0)
FROM orders o
INNER JOIN users u ON o.user_id = u.id
WHERE MONTH(CONVERT(date, DATEADD(hour, 7, o.created_at))) = MONTH(CONVERT(date, GETDATE()))
AND o.status = 3 AND u.role = 'USER'
END

go
declare 
@total_user INTEGER,
@today_register INTEGER,
@year_revenue FLOAT , 
@month_revenue FLOAT 

exec sp_summary_dashboard @total_user 
output,@today_register output,
@year_revenue output,
@month_revenue output

print @total_user
print @today_register
print @year_revenue
print @month_revenue
select @total_user,@today_register,@year_revenue,@month_revenue
