package com.aptech.coursemanagementserver.models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.aptech.coursemanagementserver.enums.NotificationType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Notifications_UserTo"))
    private User userTo;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Notifications_UserFrom"))
    private User userFrom;

    private NotificationType notificationType;

    @Builder.Default
    private boolean isDelivered = false;
    @Builder.Default
    private boolean isRead = false;

    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
}
