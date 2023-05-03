package com.example.caloriecalculator.registration;


import com.example.caloriecalculator.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegistrationFinishedEvent extends ApplicationEvent {

    private String url;
    private User user;

    public RegistrationFinishedEvent(String url, User user) {
        super(user);
        this.url = url;
        this.user = user;
    }
}
