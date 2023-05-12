package com.example.caloriecalculator.service.interfaces;

import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;

public interface IVerificationTokenService {

    VerificationToken generateTokenForUser(String token, User user);

    VerificationToken verifyToken(String token);

    void removeExpiredTokens();
}
