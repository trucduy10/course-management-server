package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.LessonTracking;
import com.aptech.coursemanagementserver.models.LessonTrackingId;

public interface LessonTrackingRepository extends JpaRepository<LessonTracking, Long> {

        Optional<LessonTracking> findByTrackId(LessonTrackingId trackId);

        @Query(value = """
                        SELECT lt.* FROM lesson_tracking lt
                        WHERE lt.enrollment_id = :enrollmentId AND lt.course_id = :courseId AND lt.is_completed = 1
                                """, nativeQuery = true)
        List<LessonTracking> findAllCompletedByEnrollmentIdAndCourseId(long enrollmentId, long courseId);

        @Query(value = """
                        SELECT TOP 1 lt.* FROM lesson_tracking lt
                        WHERE lt.enrollment_id = :enrollmentId
                        AND lt.course_id = :courseId
                        AND lt.lesson_id = :lessonId
                        ORDER BY is_tracked DESC
                                """, nativeQuery = true)
        LessonTracking findTrackedByEnrollmentIdAndCourseIdAndLessonId(long enrollmentId, long courseId, long lessonId);

        @Query(value = """
                        SELECT lt.* FROM lesson_tracking lt
                        WHERE lt.enrollment_id = :enrollmentId
                        AND lt.course_id = :courseId
                        AND lt.is_tracked = 1

                                """, nativeQuery = true)
        LessonTracking findTrackedByEnrollmentIdAndCourseId(long enrollmentId, long courseId);

        @Query(value = """
                        SELECT lt.* FROM lesson_tracking lt
                        WHERE lt.is_tracked = 1
                                """, nativeQuery = true)
        LessonTracking findTracked();
}
