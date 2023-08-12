package com.aptech.coursemanagementserver.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LessonTrackingId {
    private long enrollment_id;
    private long course_id;
    private long section_id;
    private long lession_id;
    private long video_id;

    @Override
    public String toString() {
        return "LessonTrackingId [enrollment_id=" + enrollment_id + ", course_id=" + course_id + ", section_id="
                + section_id + ", lession_id=" + lession_id + ", video_id=" + video_id + "]";
    }

}
