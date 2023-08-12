package com.aptech.coursemanagementserver.services.schedules;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.repositories.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoveDeliveredNotificationTask {
    private final NotificationRepository notifRepository;

    @Scheduled(initialDelay = 6000000, fixedRate = 6000000)
    public void deleteDeliveredNotification() throws InterruptedException {
        notifRepository.deleteDeliveredProcess();
    }
}
