package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.coursemanagementserver.dtos.RatingStarsInterface;
import com.aptech.coursemanagementserver.models.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  @Query(value = """
      SELECT * FROM enrollment
      WHERE course_id =:courseId
      AND user_id =:userId
          """, nativeQuery = true)
  Enrollment getEnrollByCourseIdAndUserId(long courseId, long userId);

  @Modifying
  @Transactional
  @Query(value = """
      UPDATE c
      SET c.rating = isnull( (
        SELECT AVG(e.rating)
        FROM enrollment e
        JOIN users u ON e.user_id = u.id
        WHERE e.course_id = c.id
        AND u.role = 'USER'
      ),0)
      FROM course c
              """, nativeQuery = true)
  void ratingProcess();

  @Query(value = """
      with rating as
      (
      SELECT COUNT(ISNULL(s.Rating,0)) CNT, s.Rating FROM
      (
      SELECT
      CASE WHEN(e.rating > 0 AND e.rating <= 1)  THEN 1
      WHEN (e.rating > 1 AND e.rating <= 2) THEN 2
      WHEN (e.rating > 2 AND e.rating <= 3)  THEN 3
      WHEN (e.rating > 3 AND e.rating <= 4)  THEN 4
      ELSE 5
      END  as Rating
      FROM enrollment e

      WHERE e.course_id = :courseId) s
      GROUP BY s.Rating
      )

      SELECT a.Rating star,
      ISNULL(ROUND( r.cnt * 100/ CAST((SELECT SUM(cnt) FROM rating) AS float),2) ,0) AS ratio
      FROM rating r RIGHT JOIN (
      SELECT 1 AS Rating, 0 AS CNT
      UNION ALL
      SELECT 2 AS Rating, 0 AS CNT
      UNION ALL
      SELECT 3 AS Rating, 0 AS CNT
      UNION ALL
      SELECT 4 AS Rating, 0 AS CNT
      UNION ALL
      SELECT 5 AS Rating, 0 AS CNT
      ) AS A ON r.Rating = a.Rating
        """, nativeQuery = true)
  List<RatingStarsInterface> getRatingPercentEachStarsByCourseId(long courseId);

}
