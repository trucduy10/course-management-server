package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.coursemanagementserver.dtos.NotificationInterface;
import com.aptech.coursemanagementserver.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

        Optional<Notification> findById(long id);

        @Query(value = """
                        SELECT n.* FROM notifications n
                        WHERE n.user_to_id = :id
                        """, nativeQuery = true)
        List<Notification> findByUserToId(long id);

        @Query(value = """
                        SELECT n.* FROM notifications n
                        WHERE n.user_to_id = :id
                        AND is_delivered = 0
                        """, nativeQuery = true)
        List<Notification> findByUserToIdAndDeliveredFalse(long id);

        @Query(value = """
                        SELECT user_to_id userToId, user_from_id userFromId FROM notifications
                        WHERE id = :id
                        """, nativeQuery = true)
        NotificationInterface getUserFromIdAndUserToId(long id);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE n
                        SET n.is_delivered = 1
                        FROM notifications n
                        WHERE n.id = :id AND n.is_read = 1
                                """, nativeQuery = true)
        void deliveredReadProcessByNotifId(long id);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE n
                        SET n.is_delivered = 1
                        FROM notifications n
                        WHERE n.user_to_id = :id AND n.is_read = 1
                                """, nativeQuery = true)
        void deliveredAllReadProcessByUserToId(long id);

        @Modifying
        @Transactional
        @Query(value = """
                        DELETE n
                        FROM notifications n
                        WHERE n.is_delivered = 1
                                """, nativeQuery = true)
        void deleteDeliveredProcess();

        @Modifying
        @Transactional
        @Query(value = """
                        INSERT [dbo].[notifications] ( [content], [created_at], [is_delivered], [is_read],
                        [notification_type], [user_from_id], [user_to_id])
                        SELECT 'New Course from ' + a.name , GETUTCDATE(), 0, 0, 4, 1, s.user_id
                        FROM users u
                        INNER JOIN subcribes s ON s.user_id = u.id
                        INNER JOIN author a ON s.author_id = a.id
                        INNER JOIN course c ON a.id = c.author_id
                        WHERE c.author_id = :authorId AND u.role = 'USER'
                        AND c.id = :courseId
                        AND u.is_notify = 1
                                """, nativeQuery = true)
        void pushCourseNotificationToUser(long authorId, long courseId);

}