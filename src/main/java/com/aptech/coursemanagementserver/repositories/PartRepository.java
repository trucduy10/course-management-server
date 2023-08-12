package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.coursemanagementserver.models.Part;

public interface PartRepository extends JpaRepository<Part, Long> {
        @Query(value = """
                        SELECT p.* FROM part p WHERE p.course_id = :courseId
                        ORDER BY created_at DESC
                        """, nativeQuery = true)
        List<Part> findByCourseId(long courseId);

        @Query(value = """
                        SELECT p.* FROM part p WHERE p.course_id = :courseId AND p.status = 1
                        """, nativeQuery = true)
        List<Part> findActivePartByCourseId(long courseId);
}
