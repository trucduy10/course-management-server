package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findAchievementByName(String name);

    @Query(value = """
            SELECT a.* FROM Achievement a INNER JOIN course_achievement c
            ON a.id = c.achievement_id
            WHERE a.name =:name AND c.course_id = :courseId
                """, nativeQuery = true)
    Achievement findAchievementByNameAndCourseId(String name, long courseId);
}