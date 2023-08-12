package com.aptech.coursemanagementserver.services;

import com.aptech.coursemanagementserver.dtos.LearningDto;
import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;

public interface LessonTrackingService {
    LessonTrackingDto loadTrack(LessonTrackingDto lessonTrackingDto);

    LessonTrackingDto saveTrackingLesson(LessonTrackingDto id);

    LessonTrackingDto saveTrack(LessonTrackingDto lessonTrackingDto);

    double complete(LessonTrackingDto lessonTrackingDto);

    double loadProgress(long enrollmentId, long courseId);

    LearningDto getLearnDetails(long courseId, long enrollId);

    LearningDto getLearnDetails(long courseId);
}
