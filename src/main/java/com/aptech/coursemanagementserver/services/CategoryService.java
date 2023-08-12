package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.CategoryDto;

public interface CategoryService {
    public CategoryDto findById(long id);

    public List<CategoryDto> findAll();

    public void save(CategoryDto categoryDto);

    public void deleteCategory(long categoryId);

    public void saveAll(List<CategoryDto> categories);

}
