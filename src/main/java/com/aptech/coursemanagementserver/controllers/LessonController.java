package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
@Tag(name = "Lesson Endpoints")
@RequestMapping("section/{sectionId}/lesson")
@Slf4j
public class LessonController {
    private final LessonService lessonService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get All Lessons By Section Id")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LessonDto>> getLessonsBySectionId(
            @PathVariable("sectionId") long sectionId) {
        try {
            return new ResponseEntity<List<LessonDto>>(lessonService.findAllBySectionId(sectionId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get Last lessonn Id")
    @GetMapping(path = "/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> getLastLessonId() {
        try {
            return new ResponseEntity<Long>(lessonService.findLastLessonId(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get Lesson By Id")
    @GetMapping(path = "/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonDto> getLessonById(
            @PathVariable("lessonId") long lessonId) {
        try {
            return new ResponseEntity<LessonDto>(lessonService.findById(lessonId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get Max Ordered Lesson By SectionId")
    @GetMapping(path = "/max-ordered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getMaxLessonOrderedBySectionId(@PathVariable("sectionId") long sectionId) {
        try {
            return new ResponseEntity<Integer>(lessonService.findMaxLessonOrderedBySectionId(sectionId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Create Lesson")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> create(@RequestBody LessonDto lessonDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(lessonService.save(lessonDto), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Update Lesson")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> update(
            @RequestBody LessonDto lessonDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(lessonService.updateLesson(lessonDto), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Delete Lesson")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> delete(long lessonId) {
        try {
            return new ResponseEntity<BaseDto>(lessonService.delete(lessonId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
