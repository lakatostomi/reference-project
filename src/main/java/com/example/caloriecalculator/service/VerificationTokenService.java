package com.example.caloriecalculator.service;

import com.example.caloriecalculator.exception.TokenHasExpiredException;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;
import com.example.caloriecalculator.repositories.VerificationTokenRepository;
import com.example.caloriecalculator.service.interfaces.IVerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@EnableScheduling
@Slf4j
public class VerificationTokenService implements IVerificationTokenService {

    private VerificationTokenRepository repository;

    @Override
    public VerificationToken generateTokenForUser(String token, User user) {
        VerificationToken verificationToken = repository.save(new VerificationToken(token, user));
        log.debug("Token has generated: " + verificationToken.toString());
        return verificationToken;
    }

    @Override
    public VerificationToken verifyToken(String token) {
        VerificationToken verificationToken = repository.findByToken(token);
        if (verificationToken == null) {
            throw new NoSuchElementException("The token is not exist!");
        }
        if (verificationToken.getExpiryDate().before(new Date())) {
            throw new TokenHasExpiredException("Token has expired!");
        }
        return verificationToken;
    }

    @Override
    @Scheduled(cron = "0 0 6 1W * *")//runs at 6 AM at first weekday of every month
    public void removeExpiredTokens() {
        log.debug("Scheduling process has started at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss V Z")));
        repository.deleteIfExpired(Timestamp.valueOf(LocalDateTime.now()));
    }
}
