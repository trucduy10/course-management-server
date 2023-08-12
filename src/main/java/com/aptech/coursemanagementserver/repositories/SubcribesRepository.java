package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Subcribes;

public interface SubcribesRepository extends JpaRepository<Subcribes, Long> {
    Optional<Subcribes> findByAuthorIdAndUserId(long authorId, long userId);

    List<Subcribes> findByUserId(long userId);

    List<Subcribes> findByAuthorId(long authorId);
}