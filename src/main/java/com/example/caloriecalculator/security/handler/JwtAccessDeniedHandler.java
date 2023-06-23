package com.example.caloriecalculator.security.handler;

import com.example.caloriecalculator.util.RestResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[{}] is trying to access to [{}] with roles [{}]",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getRequestURI(),
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(ga-> ga.getAuthority()).collect(Collectors.joining(";")));

        response.setContentType("application/problem+json");
        response.setStatus(FORBIDDEN.value());
        ProblemDetail problemDetail = RestResponseUtil.createProblemDetail(FORBIDDEN.value(), "You do not have permission to access this page!");

        RestResponseUtil.sendHttpResponse(response, problemDetail);
    }
}
