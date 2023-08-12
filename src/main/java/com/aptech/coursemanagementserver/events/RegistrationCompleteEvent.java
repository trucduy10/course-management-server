package com.aptech.coursemanagementserver.events;

import org.springframework.context.ApplicationEvent;

import com.aptech.coursemanagementserver.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;

    public RegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}
