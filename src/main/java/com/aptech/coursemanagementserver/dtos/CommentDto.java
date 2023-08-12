package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import com.aptech.coursemanagementserver.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private long id;

    private long userId;
    private String userName;
    private String imageUrl;

    @Builder.Default
    private Role role = Role.USER;

    private String content;

    private long postId;

    @Builder.Default
    private Instant created_at = Instant.now();
}
