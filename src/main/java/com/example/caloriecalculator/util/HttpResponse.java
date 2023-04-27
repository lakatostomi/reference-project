package com.example.caloriecalculator.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss a")
    private Date when;
    private int statusCode;
    private HttpStatus httpStatus;
    private String message;

    public HttpResponse(int statusCode, HttpStatus httpStatus, String message) {
        this.when = new Date();
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
