package com.example.caloriecalculator.exception;

public class AccountIsUnavailableException extends RuntimeException{

    public AccountIsUnavailableException(String message) {
        super(message);
    }
}
