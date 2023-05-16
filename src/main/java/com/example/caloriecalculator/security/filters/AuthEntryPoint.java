package com.example.caloriecalculator.security.filters;

import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.util.RestResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED,
                authException.getMessage() + (authException.getCause() == null ? "!" : authException.getCause().getMessage()));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        RestResponseUtil.sendHttpResponse(response, httpResponse);
    }
}
