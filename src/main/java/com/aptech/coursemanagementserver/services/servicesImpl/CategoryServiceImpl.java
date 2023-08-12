package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.CategoryDto;
import com.aptech.coursemanagementserver.enums.Role;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.models.Category;
import com.aptech.coursemanagementserver.models.Enrollment;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.CategoryRepository;
import com.aptech.coursemanagementserver.services.CategoryService;
import com.aptech.coursemanagementserver.services.authServices.UserService;
import com.github.slugify.Slugify;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Override
    public CategoryDto findById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        "This category with categoryId: [" + id + "] is not exist."));

        return toDto(category);
    }

    @Override
    public List<CategoryDto> findAll() {
        List<Category> categorys = categoryRepository.findAll();

        List<CategoryDto> categoryDtos = new ArrayList<>();

        for (Category category : categorys) {
            CategoryDto categoryDto = toDto(category);
            categoryDtos.add(categoryDto);
        }

        return categoryDtos;
    }

    @Override
    public void save(CategoryDto categoryDto) {
        User user = userService.findCurrentUser();

        Category category = new Category();
        if (categoryDto.getId() > 0) {
            category = categoryRepository.findById(categoryDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This category with categoryId: [" + categoryDto.getId() + "] is not exist."));
        }
        if (categoryDto.getId() == 0
                && findAll().stream().map(categoryr -> categoryr.getName()).toList().contains(categoryDto.getName())) {
            throw new IsExistedException(categoryDto.getName());
        }

        category.setName(categoryDto.getName());
        category.setImage(categoryDto.getImage());
        category.setSlug(Slugify.builder().build().slugify(categoryDto.getName()));
        category.setDescription(categoryDto.getDescription());
        category.setUpdatedBy(user.getEmail().split("@")[0]);
        categoryRepository.save(category);
    }

    @Override
    public void saveAll(List<CategoryDto> categoryDtos) {
        List<Category> categorys = new ArrayList<>();

        for (CategoryDto categoryDto : categoryDtos) {
            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setImage(categoryDto.getImage());
            category.setSlug(Slugify.builder().build().slugify(categoryDto.getName()));
            category.setDescription(categoryDto.getDescription());
            categorys.add(category);
        }
        categoryRepository.saveAll(categorys);
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NoSuchElementException("This category with categoryId: [" + categoryId + "] is not exist."));
        category.getCourses().forEach(c -> {
            Stream<Enrollment> filter = c.getEnrollments().stream().filter(e -> e.getUser().getRole() == Role.USER);
            if (filter.count() > 0) {
                throw new BadRequestException("Cannot delete category which has already had enrollment.");
            }
        });

        categoryRepository.delete(category);
    }

    private CategoryDto toDto(Category category) {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .image(category.getImage())
                .slug(category.getSlug())
                .description(category.getDescription())
                .created_at(category.getCreatedAt())
                .updatedBy(category.getUpdatedBy())
                .build();
        return categoryDto;
    }

}
