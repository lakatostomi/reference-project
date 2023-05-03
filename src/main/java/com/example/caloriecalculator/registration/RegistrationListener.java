package com.example.caloriecalculator.registration;

import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationFinishedEvent> {

    private JavaMailSender mailSender;

    private VerificationTokenService tokenService;

    @Value("${spring.mail.username}")
    private String SENDER;

    public RegistrationListener(JavaMailSender mailSender, VerificationTokenService tokenService) {
        this.mailSender = mailSender;
        this.tokenService = tokenService;
    }

    @Override
    public void onApplicationEvent(RegistrationFinishedEvent event) {
        String token = UUID.randomUUID().toString();
        tokenService.generateTokenForUser(token, event.getUser());
        sendVerificationTokenToUser(event.getUser(), event.getUrl(), token);
    }

    private void sendVerificationTokenToUser(User user, String url, String token) {
        String text = "Thanks for your registration! In order to complete the process please click on the link below!";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(SENDER);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Confirm registration");
        mailMessage.setText(text + "\n" + url + "/confirm?token=" + token);
        mailSender.send(mailMessage);
    }
}
