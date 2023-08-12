package com.aptech.coursemanagementserver.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.PartDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.PartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

@RestController
@RequiredArgsConstructor
@RequestMapping("course/{courseId}/part")
@Tag(name = "Part Endpoints")
public class PartController {
    private final PartService partService;

    @GetMapping
    @Operation(summary = "[ANY ROLE] - GET All Parts By CourseId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<PartDto>> getPartsByCourseId(@PathVariable("courseId") long courseId) {
        try {
            List<PartDto> partDtos = partService.findAllByCourseId(courseId);
            return ResponseEntity.ok(partDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "[ANY ROLE] - GET Part By Id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<PartDto> getPartById(@PathVariable("id") long id) {
        try {
            PartDto partDto = partService.findById(id);
            return ResponseEntity.ok(partDto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_EXAM] - Create / Update Part")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_EXAM')")
    public ResponseEntity<BaseDto> create(@RequestBody PartDto partDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            partService.save(partDto);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message(
                            (partDto.getId() == 0 ? "Create new" : "Update") + " part successfully.").build(),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_EXAM] - Delete Part")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_EXAM')")
    public ResponseEntity<BaseDto> delete(long partId) {
        try {
            partService.deletePart(partId);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message("Delete part successfully.").build(),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
