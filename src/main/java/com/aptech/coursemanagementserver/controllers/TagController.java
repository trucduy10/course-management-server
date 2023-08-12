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

import com.aptech.coursemanagementserver.dtos.TagDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
@Tag(name = "Tag Endpoints")
@RequestMapping("/tag")
@Slf4j
public class TagController {
    private final TagService tagService;

    @GetMapping
    @Operation(summary = "[ANORNYMOUS] - GET All Tags")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<TagDto>> getCourses() {
        try {
            List<TagDto> tagDtos = tagService.findAll();
            return ResponseEntity.ok(tagDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "[ANY ROLE] - GET Tag By Id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<TagDto> getCourseById(@PathVariable("id") long id) {
        try {
            TagDto tagDto = tagService.findById(id);
            return ResponseEntity.ok(tagDto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Create Tag")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> create(@RequestBody TagDto tagDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(tagService.create(tagDto), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Update Tag")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> update(
            @RequestBody TagDto tagDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(tagService.update(tagDto), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Delete Tag")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> delete(long tagId) {
        try {
            return new ResponseEntity<BaseDto>(tagService.delete(tagId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
