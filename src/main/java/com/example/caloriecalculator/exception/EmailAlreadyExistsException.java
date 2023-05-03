package com.example.caloriecalculator.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("An account for that email already exists: [" + email + "]!");
    }
}
