package com.example.caloriecalculator.security.handler;

import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.util.RestResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[{}] is trying to access to [{}] with roles [{}]",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getRequestURI(),
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(ga-> ga.getAuthority()).collect(Collectors.joining(";")));
        HttpResponse httpResponse = new HttpResponse(
                FORBIDDEN.value(),
                FORBIDDEN,
                "You do not have permission to access this page!"
        );

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());

        RestResponseUtil.sendHttpResponse(response, httpResponse);
    }
}
