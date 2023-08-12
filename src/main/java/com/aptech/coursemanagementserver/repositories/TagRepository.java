package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findTagByName(String name);
}
