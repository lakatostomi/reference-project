package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.exception.AccountIsUnavailableException;
import com.example.caloriecalculator.exception.EmailAlreadyExistsException;
import com.example.caloriecalculator.exception.TokenHasExpiredException;
import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.util.RestResponseUtil;
import com.mysql.cj.exceptions.NumberOutOfRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
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

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.add("Content-Type", "application/json");
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Required request body is missing!"));
        log.warn(response);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.add("Content-Type", "application/json");
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage()));
        log.warn(response);
        return handleExceptionInternal(ex, response, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.add("Content-Type", "application/json");
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage()));
        log.warn(response);
        return handleExceptionInternal(ex, response, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.add("Content-Type", "application/json");
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage()));
        log.warn(response);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.add("Content-Type", "application/json");
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage()));
        log.warn(response);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class, EmailAlreadyExistsException.class, TokenHasExpiredException.class, NumberOutOfRange.class, EntityNotFoundException.class, AccountIsUnavailableException.class})
    public ResponseEntity<Object> handleBadRequests(Exception ex, WebRequest request) {
        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage()));
        log.warn(response);
        return handleExceptionInternal(ex, response, createHeader(), HttpStatus.BAD_REQUEST, request);
    }

    private String createStringFromBindingResult(BindingResult result) {
        if (!result.hasFieldErrors()) {
            return result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        } else {
            return result.getFieldErrors().stream().map(error -> error.getField() + " field: " + error.getDefaultMessage()).collect(Collectors.joining("; "));
        }
    }

    private HttpHeaders createHeader() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("Content-Type", "application/json");
        return new HttpHeaders(map);
    }
}
