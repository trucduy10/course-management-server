package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.BlogDto;
import com.aptech.coursemanagementserver.dtos.BlogsInterface;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Blog;

public interface BlogService {
    public Blog findBlogByName(String blogName);

    public List<BlogDto> findAll();

    public BaseDto create(BlogDto blogDto);

    public BaseDto update(BlogDto blogDto);

    public BaseDto delete(long blogId);

    public BlogDto findById(long blogId);

    public BlogDto findBySlug(String slug);

    public List<BlogDto> findAllBlogsByUserId(long userId);

    public List<BlogsInterface> findAllBlogsWithRole();

    public List<BlogsInterface> findAllBlogs();

    public BaseDto updateViewCount(String slug);
}
