package com.example.caloriecalculator.security.filters;

import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.util.RestResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/problem+json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ProblemDetail problemDetail = RestResponseUtil.createProblemDetail(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());

        RestResponseUtil.sendHttpResponse(response, problemDetail);
    }
}
