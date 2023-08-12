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

import com.aptech.coursemanagementserver.dtos.BlogDto;
import com.aptech.coursemanagementserver.dtos.BlogsInterface;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Tag(name = "Blog Endpoints")
@RequestMapping("/blog")
@Slf4j
public class BlogController {
    private final BlogService blogService;

    @GetMapping(path = "blogs")
    @Operation(summary = "[ANORNYMOUS] - GET All Blogs Different by Role")
    public ResponseEntity<List<BlogsInterface>> getAllBlogs() {
        try {
            return ResponseEntity.ok(blogService.findAllBlogsWithRole());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "blogs-general")
    @Operation(summary = "[ANORNYMOUS] - GET All Blogs General")
    public ResponseEntity<List<BlogsInterface>> getAllBlogsGeneral() {
        try {
            return ResponseEntity.ok(blogService.findAllBlogs());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PutMapping(path = "view-count/{slug}")
    @Operation(summary = "[ANORNYMOUS] - Update view count Blog")
    public ResponseEntity<BaseDto> updateViewCount(@PathVariable("slug") String slug) {
        try {
            return ResponseEntity.ok(blogService.updateViewCount(slug));
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "blogs-admin")
    @Operation(summary = "[ANYROLE] - GET All Blogs Not Token")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<BlogsInterface>> getAllBlogsAdmin() {
        try {
            return ResponseEntity.ok(blogService.findAllBlogs());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    // @GetMapping(path = "/{id}")
    // @Operation(summary = "[ANORNYMOUS] - GET Blog By Id")
    // public ResponseEntity<BlogDto> getBlogById(@PathVariable("id") long id) {
    // try {
    // BlogDto blogDto = blogService.findById(id);
    // return ResponseEntity.ok(blogDto);
    // } catch (NoSuchElementException e) {
    // throw new ResourceNotFoundException(e.getMessage());
    // } catch (Exception e) {
    // throw new BadRequestException(FETCHING_FAILED);
    // }
    // }

    @GetMapping(path = "/{slug}")
    @Operation(summary = "[ANORNYMOUS] - GET Blog By Slug")
    public ResponseEntity<BlogDto> getBlogBySlug(@PathVariable("slug") String slug) {
        try {
            BlogDto blogDto = blogService.findBySlug(slug);
            return ResponseEntity.ok(blogDto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ANYROLE] - Create Blog")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> create(@RequestBody BlogDto blogDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(blogService.create(blogDto), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "[ANYROLE] - Update Blog")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_BLOG')")
    public ResponseEntity<BaseDto> update(
            @RequestBody BlogDto blogDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(blogService.update(blogDto), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "[ANY ROLE] - Delete Blog")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMP_BLOG')")
    public ResponseEntity<BaseDto> delete(@PathVariable("id") long blogId) {
        try {
            return new ResponseEntity<BaseDto>(blogService.delete(blogId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping(path = "my-blog/{userId}")
    @Operation(summary = "[ANY ROLE] - GET All Blogs By UserId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<BlogDto>> getAllUserBlogs(@PathVariable("userId") long userId) {
        try {
            return ResponseEntity.ok(blogService.findAllBlogsByUserId(userId));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
