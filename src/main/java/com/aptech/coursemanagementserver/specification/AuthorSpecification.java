package com.aptech.coursemanagementserver.specification;

import org.springframework.data.jpa.domain.Specification;

import com.aptech.coursemanagementserver.dtos.AuthorRequestDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Author;
import com.aptech.coursemanagementserver.models.Course;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class AuthorSpecification {

    // private AuthorRequestDto criteria;

    // @Override
    // @Nullable
    // public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> query,
    // CriteriaBuilder builder) {
    // if (criteria.getOperation().equals("%")) {
    // return builder.like(root.<String>get(criteria.getSearchKey()), "%" +
    // criteria.getSearchValue() + "%");
    // } else if (criteria.getOperation().equals(":")) {
    // return builder.equal(root.<String>get(criteria.getSearchKey()),
    // criteria.getSearchValue());
    // }
    // return null;
    // }

    public static Specification<Author> hasCategoryAndName(AuthorRequestDto criteria) {
        return (root, query, builder) -> {
            try {
                query.distinct(true);
                if (criteria.getCategoryId() != 0 && !criteria.getSearchKey().isBlank()
                        && !criteria.getSearchValue().isBlank()) {
                    Join<Author, Course> authorJoin = root.join("courses", JoinType.INNER);
                    Predicate coursePredicate = builder.equal(authorJoin.get("category"), criteria.getCategoryId());
                    Predicate searchValuePredicate = builder.like(root.get(criteria.getSearchKey()),
                            "%" + criteria.getSearchValue() + "%");
                    return builder.and(coursePredicate, searchValuePredicate);

                } else if (criteria.getCategoryId() == 0 && !criteria.getSearchKey().isBlank()
                        && !criteria.getSearchValue().isBlank()) {

                    return builder.like(root.get(criteria.getSearchKey()),
                            "%" + criteria.getSearchValue() + "%");

                } else if (criteria.getCategoryId() != 0 && criteria.getSearchValue().isBlank()) {
                    Join<Author, Course> authorJoin = root.join("courses", JoinType.INNER);

                    return builder.equal(authorJoin.get("category"), criteria.getCategoryId());
                }

                return null;
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }

        };
    }

}
