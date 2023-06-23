package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.exception.AccountIsUnavailableException;
import com.example.caloriecalculator.exception.EmailAlreadyExistsException;
import com.example.caloriecalculator.exception.TokenHasExpiredException;
import com.example.caloriecalculator.util.RestResponseUtil;
import com.mysql.cj.exceptions.NumberOutOfRange;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionController  {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = RestResponseUtil.createProblemDetail(HttpStatus.BAD_REQUEST.value(), createStringFromBindingResult(ex.getBindingResult()));
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, MissingPathVariableException.class})
    protected ProblemDetail handleMethodArgumentNotValid(Exception ex) {
        ProblemDetail problemDetail = RestResponseUtil.createProblemDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ProblemDetail handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ProblemDetail problemDetail = RestResponseUtil.createProblemDetail(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ProblemDetail handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        ProblemDetail problemDetail = RestResponseUtil.createProblemDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }


    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class, EmailAlreadyExistsException.class, TokenHasExpiredException.class, NumberOutOfRange.class, EntityNotFoundException.class, AccountIsUnavailableException.class})
    public ProblemDetail handleBadRequests(Exception ex) {
        ProblemDetail problemDetail = RestResponseUtil.createProblemDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    private String createStringFromBindingResult(BindingResult result) {
        if (!result.hasFieldErrors()) {
            return result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        } else {
            return result.getFieldErrors().stream().map(error -> error.getField() + " field: " + error.getDefaultMessage()).collect(Collectors.joining("; "));
        }
    }

}
