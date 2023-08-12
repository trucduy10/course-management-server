package com.aptech.coursemanagementserver.dtos;

public interface CourseInterface {
    public long getId();

    public int getEnrollmentCount();

    public String getName();

    public String getTags();

    public String getAchievements();

    public String getDuration();

    public int getStatus();

    public int getLevel();

    public long getCategory_id();

    public String getCategory_name();

    public String getCategory_slug();

    public String getCategory_image();

    public String getCategory_description();

    public long getAuthor_id();

    public String getAuthor_name();

    public String getAuthor_image();

    public String getDescription();

    public String getSlug();

    public String getImage();

    public long getProgress();

    public String getComment();

    public double getRating();

    public double getUserRating();

    public double getPrice();

    public double getNet_price();

    public String getRequirement();

    public String getUpdated_by();
}