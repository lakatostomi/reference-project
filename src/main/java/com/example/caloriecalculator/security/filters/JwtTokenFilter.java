package com.example.caloriecalculator.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.caloriecalculator.security.Auth0JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final Auth0JwtUtils jwtUtils;

    public JwtTokenFilter(Auth0JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, JWTVerificationException {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null ) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.substring(("Bearer ").length());
            DecodedJWT decodedJWT = jwtUtils.validateJwtToken(token);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String email = decodedJWT.getClaim("email").asString();
                String roles = decodedJWT.getClaim("roles").asString();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                        null,
                        createAuthorities(roles));

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private Collection<SimpleGrantedAuthority> createAuthorities(String roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles.split(";")) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
