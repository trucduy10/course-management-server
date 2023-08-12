package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.NotificationDto;
import com.aptech.coursemanagementserver.dtos.UserDto;
import com.aptech.coursemanagementserver.models.Notification;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.NotificationRepository;
import com.aptech.coursemanagementserver.services.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifRepository;

    public Notification save(Notification notification) {
        return notifRepository.save(notification);
    }

    public NotificationDto findById(long id) {
        Notification notification = notifRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "The notification with notificationId: [" + id + "] is not exist."));
        return toNotifDto(notification);
    }

    public List<NotificationDto> findAllByUserIdNotRead(long userToId) {
        List<Notification> notifications = notifRepository.findByUserToIdAndDeliveredFalse(userToId);

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = toNotifDto(notification);
            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }

    public List<NotificationDto> findAllByUserToId(long userID) {
        List<Notification> notifications = notifRepository.findByUserToId(userID);

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = toNotifDto(notification);
            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }

    public NotificationDto updateStatusToRead(long id) {
        var notif = notifRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "The notification with notificationId: [" + id + "] is not exist."));
        notif.setRead(true);
        notif.setDelivered(false);
        notifRepository.save(notif);

        new Thread(() -> {
            try {
                Thread.sleep(10000);
                notifRepository.deliveredReadProcessByNotifId(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        NotificationDto notificationDto = toNotifDto(notif);
        return notificationDto;
    }

    public void deleteByNotifId(long id) {
        var notif = notifRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "The notification with notificationId: [" + id + "] is not exist."));

        notifRepository.delete(notif);
    }

    public void deleteAllByUserId(long userId) {
        List<Notification> notifs = notifRepository.findByUserToId(userId);
        notifRepository.deleteAll(notifs);
    }

    public List<NotificationDto> updateAllStatusToRead(long userToId) {
        List<Notification> notifications = notifRepository.findByUserToId(userToId);

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            notification.setRead(true);
            notification.setDelivered(false);

            NotificationDto notificationDto = toNotifDto(notification);
            notificationDtos.add(notificationDto);
        }

        notifRepository.saveAll(notifications);
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                notifRepository.deliveredAllReadProcessByUserToId(notifications.get(0).getUserTo().getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return notificationDtos;
    }

    public void clear() {
        notifRepository.deleteAll();
    }

    private NotificationDto toNotifDto(Notification notification) {
        User userFrom = notification.getUserFrom();
        UserDto userFromDto = toUserDto(userFrom);

        User userTo = notification.getUserTo();
        UserDto userToDto = toUserDto(userTo);

        NotificationDto notifDto = NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .isDelivered(notification.isDelivered())
                .isRead(notification.isRead())
                .notificationType(notification.getNotificationType())
                .userFrom(userFromDto)
                .userTo(userToDto)
                .created_at(notification.getCreated_at()).build();
        return notifDto;
    }

    private UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .build();
        return userDto;
    }
}
