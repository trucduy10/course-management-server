package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.CategoryEnrollmentDto;
import com.aptech.coursemanagementserver.dtos.RevenueYearDto;
import com.aptech.coursemanagementserver.dtos.SearchDto;
import com.aptech.coursemanagementserver.dtos.SummaryDashboardDto;
import com.aptech.coursemanagementserver.enums.SearchType;
import com.aptech.coursemanagementserver.models.Author;
import com.aptech.coursemanagementserver.models.Blog;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.repositories.AuthorRepository;
import com.aptech.coursemanagementserver.repositories.BlogRepository;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.DashBoardRepository;
import com.aptech.coursemanagementserver.services.HomeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final AuthorRepository authorRepository;
    private final BlogRepository blogRepository;
    private final CourseRepository courseRepository;
    private final DashBoardRepository dashBoardRepository;

    @Override
    public List<SearchDto> searchAll(String name) {

        List<SearchDto> searchDtos = new ArrayList<>();
        List<Author> authors = authorRepository.findByNameLikeOrderByName(name);
        List<Blog> blogs = blogRepository.findByNameLikeOrderByName(name);
        List<Course> courses = courseRepository.findByNameLikeOrderByName(name);

        authors.stream().forEach(a -> {
            SearchDto searchDto = new SearchDto();
            searchDto.setDescription(a.getInformation())
                    .setName(a.getName())
                    .setId(a.getId())
                    .setImage(a.getImage())
                    .setType(SearchType.AUTHOR);
            searchDtos.add(searchDto);
        });

        blogs.stream().forEach(b -> {
            SearchDto searchDto = new SearchDto();
            searchDto.setDescription(b.getDescription())
                    .setName(b.getName())
                    .setId(b.getId())
                    .setImage(b.getImage())
                    .setType(SearchType.BLOG);
            searchDtos.add(searchDto);
        });

        courses.stream().forEach(c -> {
            SearchDto searchDto = new SearchDto();
            searchDto.setDescription(c.getDescription())
                    .setName(c.getName())
                    .setId(c.getId())
                    .setImage(c.getImage())
                    .setType(SearchType.COURSE);
            searchDtos.add(searchDto);
        });

        return searchDtos;
    }

    @Override
    public SummaryDashboardDto getSummaryDashboard() {
        return dashBoardRepository.getSummaryDashboard();
    }

    @Override
    public List<CategoryEnrollmentDto> getCategoryEnrollmentChart() {
        return dashBoardRepository.getCategoryEnrollment();
    }

    @Override
    public List<RevenueYearDto> getRevenueYearChart(int year) {
        return dashBoardRepository.getRevenueYear(year);
    }
}
