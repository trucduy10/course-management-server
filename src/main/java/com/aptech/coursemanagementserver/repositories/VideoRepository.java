package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findVideoByName(String name);

    @Query(value = """
            SELECT v.* FROM video v
            JOIN lesson l
            ON v.lesson_id = l.id
            JOIN section s
            ON l.section_id = s.id
            JOIN course c
            ON s.course_id = c.id

            WHERE c.id =  :courseId
                    """, nativeQuery = true)
    List<Video> findAllByCourseId(long courseId);
}