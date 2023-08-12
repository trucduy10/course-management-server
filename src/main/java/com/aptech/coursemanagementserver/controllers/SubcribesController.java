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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.SubcribesDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.SubcribesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER', 'EMPLOYEE')")
@Tag(name = "Subcribes Endpoints")
@RequestMapping("/subcribes")
public class SubcribesController {
    private final SubcribesService subcribesService;

    @GetMapping(path = "/{userId}")
    @Operation(summary = "[ANY ROLE] - GET All Subcribes By UserId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<SubcribesDto>> getSubcribesByUserId(@PathVariable("userId") long userId) {
        try {
            List<SubcribesDto> subcribesDtos = subcribesService.findByUserId(userId);
            return ResponseEntity.ok(subcribesDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "[ANY ROLE] - GET All Subcribes By AuthorId")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<SubcribesDto>> getSubcribesByAuthorId(@PathVariable("authorId") long authorId) {
        try {
            List<SubcribesDto> subcribesDtos = subcribesService.findByAuthorId(authorId);
            return ResponseEntity.ok(subcribesDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    // @GetMapping(path = "/{id}")
    // @Operation(summary = "[ANY ROLE] - GET Subcribes By Id")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    // public ResponseEntity<SubcribesDto> getSubcribesById(@PathVariable("id") long
    // id) {
    // try {
    // SubcribesDto subcribesDto = subcribesService.findById(id);
    // return ResponseEntity.ok(subcribesDto);
    // } catch (NoSuchElementException e) {
    // throw new ResourceNotFoundException(e.getMessage());
    // } catch (Exception e) {
    // throw new BadRequestException(FETCHING_FAILED);
    // }
    // }

    @PostMapping
    @Operation(summary = "[ANY ROLE] - Subcribes to Author")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> subcribe(@RequestBody SubcribesDto subcribesDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            subcribesService.subcribe(subcribesDto);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message(
                            "You have subscribed to this author.").build(),
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
    @Operation(summary = "[ANY ROLE] - Unubcribes to Author")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> unSubcribes(@RequestBody SubcribesDto subcribesDto) {
        try {
            subcribesService.unSubcribes(subcribesDto.getAuthorId(), subcribesDto.getUserId());
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message(
                            "You have unsubscribed to this author.").build(),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
