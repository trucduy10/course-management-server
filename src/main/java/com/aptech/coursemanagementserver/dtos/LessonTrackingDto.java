package com.aptech.coursemanagementserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonTrackingDto {
    // Select Lesson ->
    // notexist -> create
    // exist -> update
    // Return tracking lesson

    // private long oldSectionId;
    // private long oldLessonId;
    // private long oldVideoId;
    private long id;
    private long enrollmentId;
    private long courseId;
    private long sectionId;
    private long lessonId;
    private long videoId;
    private boolean isCompleted;
    private boolean isTracked;
    private int resumePoint;

    // Update LessonTracking set sectionId ='new SectionID', lessonId ='new
    // SectionID' where
    // courseId ='courseId' and enrollmentId= 'enrollmentId' and sectionId ='old
    // section' and lessonId = 'old lessonId';

    // old -> previous State

}
