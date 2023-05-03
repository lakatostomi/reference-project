package com.example.caloriecalculator.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private int statusCode;
    private HttpStatus httpStatus;
    private String message;

    public HttpResponse(int statusCode, HttpStatus httpStatus, String message) {
        this.time = new Date();
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
