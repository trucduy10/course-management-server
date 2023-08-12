package com.aptech.coursemanagementserver.mappers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.models.Course;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {
    private final ModelMapper modelMapper;

    public CourseDto toDto(Course course) {
        return Objects.isNull(course) ? null : modelMapper.map(course, CourseDto.class);
    }

    public List<CourseDto> toDtoList(List<Course> courseList) {
        return courseList.stream()
                .map(course -> toDto(course))
                .collect(Collectors.toList());
    }

    public Course toEntity(CourseDto courseDto) {
        return Objects.isNull(courseDto) ? null : modelMapper.map(courseDto, Course.class);
    }

    public List<Course> toEntityList(List<CourseDto> coursesDto) {
        return coursesDto.stream()
                .map(courseDto -> toEntity(courseDto))
                .collect(Collectors.toList());
    }
}
