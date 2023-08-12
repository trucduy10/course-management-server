UPDATE course set duration = a.sumDuration from course c inner join
(
select c.id, SUM(l.duration) sumDuration from course c
INNER JOIN section s ON c.id = s.course_id
INNER JOIN lesson l  ON s.id = l.section_id
GROUP BY c.id
) as A on c.id = a.id