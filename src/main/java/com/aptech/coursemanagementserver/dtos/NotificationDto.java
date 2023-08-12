package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.aptech.coursemanagementserver.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private long id;

    private String content;

    // private long userToId;
    private UserDto userTo;

    // private long userFromId;
    private UserDto userFrom;

    private NotificationType notificationType;

    @Builder.Default
    private boolean isDelivered = false;
    @Builder.Default
    private boolean isRead = false;

    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
}
