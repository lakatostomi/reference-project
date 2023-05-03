package com.example.caloriecalculator.exception;

public class TokenHasExpiredException extends RuntimeException {

    public TokenHasExpiredException(String message) {
        super(message);
    }
}
