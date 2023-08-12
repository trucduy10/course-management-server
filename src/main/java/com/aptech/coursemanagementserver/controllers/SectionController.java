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

import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.SectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
@Tag(name = "Section Endpoints")
@RequestMapping("course/{id}/section")

public class SectionController {
    private final SectionService sectionService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get All Sections By Course Id")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SectionDto>> getSectionsByCourseId(@PathVariable("id") long courseId) {
        try {
            return new ResponseEntity<List<SectionDto>>(sectionService.findAllByCourseId(courseId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Course", "courseId", Long.toString(courseId));
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get Section By Id")
    @GetMapping(path = "/{sectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SectionDto> getSectionById(@PathVariable("sectionId") long sectionId) {
        try {
            return new ResponseEntity<SectionDto>(sectionService.findById(sectionId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get Last Section Id")
    @GetMapping(path = "/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> getLastSectionId() {
        try {
            return new ResponseEntity<Long>(sectionService.findLastSectionId(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    @Operation(summary = "[ANY ROLE] - Get Max Ordered Section By CourseId")
    @GetMapping(path = "/max-ordered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getMaxOrderedSectionByCourseId(@PathVariable("id") long courseId) {
        try {
            return new ResponseEntity<Integer>(sectionService.findMaxSectionOrderedByCourseId(courseId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Create Section")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> create(
            @RequestBody SectionDto sectionDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(sectionService.saveSection(sectionDto),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping(path = "/createByCourseId")
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Create Multiple Sections By Course Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> createSectionsByCourseId(@PathVariable("id") long courseId,
            @RequestBody SectionDto sectionDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(sectionService.saveSectionsToCourseByStringSplit(sectionDto, courseId),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Course", "courseId", Long.toString(courseId));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Update Section")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> update(@RequestBody SectionDto sectionDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(sectionService.updateSection(sectionDto),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Delete Section")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> delete(long sectionId) {
        try {
            return new ResponseEntity<BaseDto>(sectionService.delete(sectionId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
