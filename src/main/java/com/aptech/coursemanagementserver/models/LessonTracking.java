package com.aptech.coursemanagementserver.models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
// @Table(indexes = {
// @Index(name = "IDX_LessonTracking_EnrollmentId_CourseId_SectionId_VideoId",
// columnList = "track_id", unique = true),
// })
@Table(uniqueConstraints = {
                @UniqueConstraint(columnNames = { "enrollment_id", "course_id", "section_id", "lesson_id",
                                "video_id" })
})
public class LessonTracking {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(columnDefinition = "bigint")
        private long id;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "enrollment_id", column = @Column(name = "enrollment_id")),
                        @AttributeOverride(name = "course_id", column = @Column(name = "course_id")),
                        @AttributeOverride(name = "section_id", column = @Column(name = "section_id")),
                        @AttributeOverride(name = "lession_id", column = @Column(name = "lesson_id")),
                        @AttributeOverride(name = "video_id", column = @Column(name = "video_id"))
        })
        LessonTrackingId trackId;
        private boolean isCompleted = false;
        private boolean isTracked = false;
        private int resumePoint = 0;

        @CreationTimestamp
        private Instant created_at = Instant.now();
        @UpdateTimestamp
        private Instant updated_at;
}
