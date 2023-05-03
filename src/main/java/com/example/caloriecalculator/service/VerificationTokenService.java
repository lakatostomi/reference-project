package com.example.caloriecalculator.service;

import com.example.caloriecalculator.exception.TokenHasExpiredException;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;
import com.example.caloriecalculator.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class VerificationTokenService {

    private VerificationTokenRepository repository;

    public VerificationTokenService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    public VerificationToken generateTokenForUser(String token, User user) {
        return repository.save(new VerificationToken(token, user));
    }

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
}
