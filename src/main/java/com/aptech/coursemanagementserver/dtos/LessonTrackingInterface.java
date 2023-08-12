package com.aptech.coursemanagementserver.dtos;

public interface LessonTrackingInterface {
    long getId();

    String getDescription();

    int getDuration();

    String getName();

    int getOrdered();

    long getSection_id();

    boolean getIs_completed();
}
