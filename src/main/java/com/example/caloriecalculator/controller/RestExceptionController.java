package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.exception.EmailAlreadyExistsException;
import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.exception.TokenHasExpiredException;
import com.example.caloriecalculator.util.RestResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionController extends ResponseEntityExceptionHandler {


    public RestExceptionController() {
        super();
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.add("Content-Type", "application/json");
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, createStringFromBindingResult(ex.getBindingResult())));
        log.warn(response);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }



    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class, EmailAlreadyExistsException.class, TokenHasExpiredException.class})
    public ResponseEntity<Object> handleNoSuchElement(Exception ex,  WebRequest request) {
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage()));
        log.warn(response);
        return handleExceptionInternal(ex, response, createHeader(), HttpStatus.BAD_REQUEST, request);
    }

    private String createStringFromBindingResult(BindingResult result) {
        if (!result.hasFieldErrors()) {
            return result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        } else {
            return result.getFieldErrors().stream().map(error -> "[" + error.getField() + " field: " + error.getDefaultMessage() + "]").collect(Collectors.joining("; "));
        }
    }

    private HttpHeaders createHeader() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("Content-Type", "application/json");
        return new HttpHeaders(map);
    }
}
