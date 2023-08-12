package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AchievementDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Achievement;
import com.aptech.coursemanagementserver.repositories.AchievementRepository;
import com.aptech.coursemanagementserver.services.AchievementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;

    @Override
    public Achievement findAchievementByName(String achievementName) {
        return achievementRepository.findAchievementByName(achievementName);
    }

    @Override
    public List<AchievementDto> findAll() {
        List<Achievement> achievements = achievementRepository.findAll();
        List<AchievementDto> achievementDtos = new ArrayList<>();
        for (Achievement achievement : achievements) {
            AchievementDto achievementDto = toAchievementDto(achievement);
            achievementDtos.add(achievementDto);
        }
        return achievementDtos;
    }

    @Override
    public BaseDto create(AchievementDto achievementDto) {
        try {
            Achievement achievement = new Achievement();
            achievement.setId(achievementDto.getId())
                    .setName(achievementDto.getName())
                    .setCreated_at(Instant.now())
                    .setUpdated_at(Instant.now());
            achievementRepository.save(achievement);
            return BaseDto.builder().type(AntType.success).message("Create achievement successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "The achievement with achievementId: [" + achievementDto.getId() + "] is not exist.");
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto update(AchievementDto achievementDto) {
        try {
            Achievement achievement = achievementRepository.findById(achievementDto.getId()).orElseThrow(() -> new NoSuchElementException(
                    "The achievement with achievementId: [" + achievementDto.getId() + "] is not exist."));
            achievement.setName(achievementDto.getName())
                    .setUpdated_at(Instant.now());

            achievementRepository.save(achievement);

            return BaseDto.builder().type(AntType.success).message("Update achievement successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto delete(long achievementId) {
        try {
            Achievement achievement = achievementRepository.findById(achievementId).orElseThrow(
                    () -> new NoSuchElementException("The achievement with achievementId: [" + achievementId + "] is not exist."));
            achievementRepository.delete(achievement);
            return BaseDto.builder().type(AntType.success).message("Delete blog successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public AchievementDto findById(long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId).orElseThrow(
                () -> new NoSuchElementException("This achievement with achievementId: [" + achievementId + "] is not exist."));
        AchievementDto achievementDto = toAchievementDto(achievement);
        return achievementDto;
    }

    private AchievementDto toAchievementDto(Achievement achievement) {
        AchievementDto achievementDto = AchievementDto.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .build();
        return achievementDto;
    }
    
}
