package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.AchievementDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Achievement;

public interface AchievementService {
    public Achievement findAchievementByName(String achievementName);

    public List<AchievementDto> findAll();

    public BaseDto create(AchievementDto achievementDto);

    public BaseDto update(AchievementDto achievementDto);

    public BaseDto delete(long achievementId);

    public AchievementDto findById(long achievementId);
}
