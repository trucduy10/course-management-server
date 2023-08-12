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

import com.aptech.coursemanagementserver.dtos.AnswerDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.AnswerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question/{questionId}/answer")
@Tag(name = "Answer Endpoints")
public class AnswerController {
    private final AnswerService answerService;

    @GetMapping
    @Operation(summary = "[ANY ROLE] - GET All Answers By QuestionId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<AnswerDto>> getAnswersByQuestionId(@PathVariable("questionId") long questionId) {
        try {
            List<AnswerDto> answerDtos = answerService.findAllByQuestionId(questionId);
            return ResponseEntity.ok(answerDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "[ANY ROLE] - GET Answer By Id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<AnswerDto> getAnswerById(@PathVariable("id") long id) {
        try {
            AnswerDto answerDto = answerService.findById(id);
            return ResponseEntity.ok(answerDto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_EXAM] - Create / Update Answer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_EXAM')")
    public ResponseEntity<BaseDto> create(@RequestBody AnswerDto answerDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            answerService.save(answerDto);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message(
                            (answerDto.getId() == 0 ? "Create new" : "Update") + " answer successfully.").build(),
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
    @Operation(summary = "[ADMIN, MANAGER, EMP_EXAM] - Delete Answer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_EXAM')")
    public ResponseEntity<BaseDto> delete(long answerId) {
        try {
            answerService.deleteAnswer(answerId);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message("Delete answer successfully.").build(),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
