package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
