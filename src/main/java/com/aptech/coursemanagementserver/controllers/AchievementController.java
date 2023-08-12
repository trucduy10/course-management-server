package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.AchievementDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.AchievementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
@Tag(name = "Achievement Endpoints")
@RequestMapping("/achievement")
@Slf4j
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    @Operation(summary = "[ANORNYMOUS] - GET All Achievements")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<AchievementDto>> getAchievements() {
        try {
            List<AchievementDto> achievementDtos = achievementService.findAll();
            return ResponseEntity.ok(achievementDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "[ANY ROLE] - GET Achievement By Id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<AchievementDto> getAchievementById(@PathVariable("id") long id) {
        try {
            AchievementDto achievementDto = achievementService.findById(id);
            return ResponseEntity.ok(achievementDto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Create Achievement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> create(@RequestBody AchievementDto achievementDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(achievementService.create(achievementDto), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Update Achievement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> update(
            @RequestBody AchievementDto achievementDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(achievementService.update(achievementDto), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Delete Achievement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> delete(long achievementId) {
        try {
            return new ResponseEntity<BaseDto>(achievementService.delete(achievementId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
