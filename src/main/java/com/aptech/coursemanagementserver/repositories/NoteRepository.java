package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.LessonTrackingId;
import com.aptech.coursemanagementserver.models.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByTrackId(LessonTrackingId trackId);

    @Query(value = """
            SELECT n.* FROM note n
            WHERE n.enrollment_id = :enrollmentId
            AND n.course_id = :courseId
                    """, nativeQuery = true)
    List<Note> findAllNotesByEnrollmentIdAndCourseId(long enrollmentId, long courseId);
}
