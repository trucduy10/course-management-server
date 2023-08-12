package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.COURSE_PATH;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.dtos.CourseInterface;
import com.aptech.coursemanagementserver.dtos.CourseRelatedDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.InvalidFileExtensionException;
import com.aptech.coursemanagementserver.exceptions.InvalidTokenException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.services.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
@Tag(name = "Course Endpoints")
public class CourseController {
        private final CourseService courseService;

        @GetMapping(path = "/oldway")
        @Operation(summary = "[ANORNYMOUS] - GET All Courses Old Way")
        public ResponseEntity<List<CourseDto>> getCourses_OldWay() {
                try {
                        List<CourseDto> courseDtos = courseService.findAll();
                        return ResponseEntity.ok(courseDtos);

                } catch (Exception e) {
                        throw new BadRequestException(FETCHING_FAILED);
                }
        }

        @GetMapping
        @Operation(summary = "[ANORNYMOUS] - GET All Courses")
        public ResponseEntity<List<CourseInterface>> getCourses() {
                try {
                        return ResponseEntity.ok(courseService.findAllCourses());
                } catch (Exception e) {
                        throw new BadRequestException(FETCHING_FAILED);
                }
        }

        @GetMapping(path = "my-course/{userId}")
        @Operation(summary = "[ANY ROLE] - GET All Courses By UserId")
        @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
        public ResponseEntity<List<CourseInterface>> getAllUserCourses(@PathVariable("userId") long userId) {
                try {
                        return ResponseEntity.ok(courseService.findAllCoursesByUserId(userId));
                } catch (NoSuchElementException e) {
                        throw new ResourceNotFoundException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(e.getMessage());
                }
        }

        @GetMapping(path = "/best-course")
        @Operation(summary = "[ANORNYMOUS] - GET Best Saler Courses")
        @PreAuthorize("permitAll()")
        public ResponseEntity<List<CourseDto>> getBestCourses() {
                try {
                        List<CourseDto> courseDtos = courseService.findBestSellerCourses();
                        return ResponseEntity.ok(courseDtos);
                } catch (Exception e) {
                        throw new BadRequestException(FETCHING_FAILED);
                }
        }

        @GetMapping(path = "/free-course")
        @Operation(summary = "[ANORNYMOUS] - GET Free Courses")
        @PreAuthorize("permitAll()")
        public ResponseEntity<List<CourseDto>> getFreeCourses() {
                try {
                        List<CourseDto> courseDtos = courseService.findFreeCourses();
                        return ResponseEntity.ok(courseDtos);
                } catch (Exception e) {
                        throw new BadRequestException(FETCHING_FAILED);
                }
        }

        @PostMapping(path = "/related-course")
        @Operation(summary = "[ANORNYMOUS] - GET Related Courses")
        @PreAuthorize("permitAll()")

        public ResponseEntity<List<CourseDto>> getRelatedCourses(@RequestBody CourseRelatedDto relatedDto) {
                try {
                        List<CourseDto> courseDtos = courseService.findRelatedCourses(relatedDto);
                        return ResponseEntity.ok(courseDtos);
                } catch (NoSuchElementException e) {
                        throw new ResourceNotFoundException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(FETCHING_FAILED);
                } // "org.springframework.orm.jpa.JpaSystemException: Null value was assigned to a
                  // property [rating] of primitive type : `Course.rating` (setter)"
        }

        @GetMapping(path = "/{id}")
        @Operation(summary = "[ANY ROLE] - GET Course By Id")
        @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")

        public ResponseEntity<CourseDto> getCourseById(@PathVariable("id") long id) {
                try {
                        CourseDto courseDto = courseService.findById(id);
                        return ResponseEntity.ok(courseDto);
                } catch (NoSuchElementException e) {
                        throw new ResourceNotFoundException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(FETCHING_FAILED);
                }

        }

        @GetMapping(path = "/slug/{slug}")
        @Operation(summary = "[ANY ROLE] - GET Course By Slug")
        @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")

        public ResponseEntity<CourseDto> getCourseBySlug(@PathVariable("slug") String slug) {
                try {
                        CourseDto courseDto = courseService.findBySlug(slug);
                        return ResponseEntity.ok(courseDto);
                } catch (NoSuchElementException e) {
                        throw new ResourceNotFoundException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(FETCHING_FAILED);
                }
        }

        @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Create Course")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
        public ResponseEntity<BaseDto> create(@RequestPart("courseJson") String courseJson)
                        throws JsonMappingException, JsonProcessingException {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                        CourseDto courseDto = objectMapper.readValue(courseJson, CourseDto.class);
                        courseService.save(courseDto);

                        return new ResponseEntity<BaseDto>(
                                        BaseDto.builder().type(AntType.success).message("Create course successfully")
                                                        .build(),
                                        HttpStatus.OK);
                        // "Cannot construct instance of
                        // `com.aptech.coursemanagementserver.dtos.CourseDto` (no Creators, like default
                        // constructor, exist): cannot deserialize from Object value (no delegate- or
                        // property-based Creator)"
                } catch (InvalidFileExtensionException e) {
                        throw new InvalidFileExtensionException(e.getMessage());
                } catch (BadRequestException e) {
                        throw new BadRequestException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(BAD_REQUEST_EXCEPTION);
                }
        }

        @GetMapping(path = "/download")
        @Operation(summary = "[ANORNYMOUS] - Load Course Image")
        @PreAuthorize("permitAll()")
        public ResponseEntity<Resource> download(@RequestParam long courseId)
                        throws MalformedURLException {
                try {
                        Course course = courseService.findCourseById(courseId);

                        String fileExtension = FilenameUtils.getExtension(course.getImage());
                        // Auto add slash
                        Path root = courseId == 1 ? COURSE_PATH.resolve("default.jpg")
                                        : COURSE_PATH.resolve(
                                                        generateFilename(course.getUpdated_at(), fileExtension,
                                                                        course));

                        Resource file = new UrlResource(root.toUri());

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                                        "attachment; filename=\"" + file.getFilename() + "\"")
                                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                        .body(file);
                } catch (NoSuchElementException e) {
                        throw new ResourceNotFoundException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(GLOBAL_EXCEPTION);
                }
        }

        @PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Update Course")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
        public ResponseEntity<BaseDto> updateCourse(@RequestPart("courseJson") String courseJson) {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                        CourseDto courseDto = objectMapper.readValue(courseJson, CourseDto.class);
                        Course course = courseService.findCourseById(courseDto.getId());
                        courseService.setProperties(courseDto, course);

                        return new ResponseEntity<BaseDto>(
                                        BaseDto.builder().type(AntType.success).message("Update course successfully.")
                                                        .build(),
                                        HttpStatus.OK);

                } catch (InvalidFileExtensionException e) {
                        throw new InvalidFileExtensionException(e.getMessage());
                } catch (NoSuchElementException e) {
                        throw new ResourceNotFoundException(e.getMessage());
                } catch (BadRequestException e) {
                        throw new BadRequestException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(BAD_REQUEST_EXCEPTION);
                }
        }

        @DeleteMapping
        @Operation(summary = "[ADMIN, MANAGER, EMP_COURSE] - Delete Course")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
        public ResponseEntity<BaseDto> deleteCourse(long courseId) {
                try {
                        return new ResponseEntity<BaseDto>(courseService.delete(courseId), HttpStatus.OK);
                } catch (InvalidTokenException e) {
                        return new ResponseEntity<BaseDto>(BaseDto.builder().type(AntType.error)
                                        .message(BAD_REQUEST_EXCEPTION)
                                        .build(), HttpStatus.BAD_REQUEST);
                } catch (NoSuchElementException e) {
                        throw new ResourceNotFoundException(e.getMessage());
                } catch (BadRequestException e) {
                        throw new BadRequestException(e.getMessage());
                } catch (Exception e) {
                        throw new BadRequestException(e.getMessage());
                }

        }

        private String generateFilename(Instant instant, String extension, Course savedCourse) {
                return instant.atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_"
                                + Slugify.builder().build().slugify(savedCourse.getName())
                                + "_" + savedCourse.getId() + "." + extension;
        }

        // @PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        // @Operation(summary = "[ADMIN, MANAGER, EMPLOYEE] - Update Course")
        // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMP_COURSE')")
        // public ResponseEntity<BaseDto> publishCourse(@RequestPart("courseJson")
        // String courseJson) {
        // ObjectMapper objectMapper = new ObjectMapper();

        // try {
        // CourseDto courseDto = objectMapper.readValue(courseJson, CourseDto.class);
        // Course course = courseService.findCourseById(courseDto.getId());
        // courseService.setProperties(courseDto, course);

        // return new ResponseEntity<BaseDto>(
        // BaseDto.builder().type(AntType.success).message("Create course successfully")
        // .build(),
        // HttpStatus.OK);

        // } catch (InvalidFileExtensionException e) {
        // throw new InvalidFileExtensionException(e.getMessage());
        // } catch (NoSuchElementException e) {
        // throw new ResourceNotFoundException(e.getMessage());
        // } catch (BadRequestException e) {
        // throw new BadRequestException(e.getMessage());
        // } catch (Exception e) {
        // throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        // }
        // }

        // private boolean isImageFile(String extension) {
        // return extension.equals("jpeg") || extension.equals("jpg") ||
        // extension.equals("png")
        // || extension.equals("gif") || extension.equals("bmp")
        // || extension.equals("tiff") || extension.equals("tif")
        // || extension.equals("webp") || extension.equals("svg");
        // }

}
