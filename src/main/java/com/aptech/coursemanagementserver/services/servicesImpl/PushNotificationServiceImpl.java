package com.aptech.coursemanagementserver.services.servicesImpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.NotificationDto;
import com.aptech.coursemanagementserver.dtos.NotificationInterface;
import com.aptech.coursemanagementserver.dtos.UserDto;
import com.aptech.coursemanagementserver.models.Notification;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.NotificationRepository;
import com.aptech.coursemanagementserver.services.PushNotificationService;
import com.aptech.coursemanagementserver.services.authServices.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public Flux<ServerSentEvent<List<NotificationDto>>> getNotificationsByUserToID(long userId) {

        if (userId != 0) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<NotificationDto>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(userId))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<NotificationDto>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }

    @Transactional
    private List<NotificationDto> getNotifs(long userId) {
        List<Notification> notifications = notificationRepository.findByUserToIdAndDeliveredFalse(userId);

        // notifications.forEach(x -> x.setDelivered(true));
        // notificationRepository.saveAll(notifications);

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = toNotifDto(notification);
            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }

    private NotificationDto toNotifDto(Notification notification) {
        NotificationInterface notificationInterface = notificationRepository
                .getUserFromIdAndUserToId(notification.getId());

        User userFrom = userService.findById(notificationInterface.getUserFromId()).get();
        UserDto userFromDto = toUserDto(userFrom);

        User userTo = userService.findById(notificationInterface.getUserToId()).get();
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
                .created_at(user.getCreated_at())
                .build();
        return userDto;
    }
}
