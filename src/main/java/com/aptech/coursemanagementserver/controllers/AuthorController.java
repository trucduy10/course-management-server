package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.AuthorDto;
import com.aptech.coursemanagementserver.dtos.AuthorInterface;
import com.aptech.coursemanagementserver.dtos.AuthorRequestDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
@Tag(name = "Author Endpoints")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "[ANORNYMOUS] - GET All Authors")
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        try {
            List<AuthorDto> authorDtos = authorService.findAll();
            return ResponseEntity.ok(authorDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping("/top3")
    @Operation(summary = "[ANORNYMOUS] - GET Top 3 Authors That have most Enrollment")
    public ResponseEntity<List<AuthorInterface>> getTop3Authors() {
        try {
            List<AuthorInterface> authorDtos = authorService.findTop3();
            return ResponseEntity.ok(authorDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping("/authors-pagination")
    @Operation(summary = "[ANORNYMOUS] - GET All Authors With Pagination")
    public ResponseEntity<Page<AuthorDto>> getAuthorsPagination(@RequestBody AuthorRequestDto dto) {
        try {
            return ResponseEntity.ok(authorService.findAllPagination(dto));
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    // @GetMapping("/authors-pagination/{categoryId}")
    // @Operation(summary = "[ANORNYMOUS] - GET All Authors With Pagination Filter
    // by CategoryId")
    // public ResponseEntity<Page<AuthorDto>>
    // getAuthorsPaginationFilter(@RequestParam(defaultValue = "0") int pageNo,
    // @RequestParam(defaultValue = "4") int pageSize, @PathVariable long
    // categoryId) {
    // try {
    // return ResponseEntity.ok(authorService.findAllPaginationFilter(pageNo,
    // pageSize, categoryId));
    // } catch (Exception e) {
    // throw new BadRequestException(FETCHING_FAILED);
    // }
    // }

    @GetMapping(path = "/{id}")
    @Operation(summary = "[ANORNYMOUS - GET Author By Id")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable("id") long id) {
        try {
            AuthorDto authorDto = authorService.findById(id);
            return ResponseEntity.ok(authorDto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Create / Update Author")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> save(@RequestBody AuthorDto authorDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            authorService.save(authorDto);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message(
                            (authorDto.getId() == 0 ? "Create new" : "Update") + " author successfully.").build(),
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
    @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Delete Author")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
    public ResponseEntity<BaseDto> delete(long authorId) {
        try {
            authorService.deleteAuthor(authorId);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message("Delete author successfully.").build(),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
