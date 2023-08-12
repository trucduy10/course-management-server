package com.aptech.coursemanagementserver.services;

import java.util.List;

import org.springframework.http.codec.ServerSentEvent;

import com.aptech.coursemanagementserver.dtos.NotificationDto;

import reactor.core.publisher.Flux;

public interface PushNotificationService {

    public Flux<ServerSentEvent<List<NotificationDto>>> getNotificationsByUserToID(long userId);
}
