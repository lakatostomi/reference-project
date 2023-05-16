package com.example.caloriecalculator.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.util.RestResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Auth0JwtUtils {

    public Auth0JwtUtils() {
        init();
    }

    private Algorithm algorithm;
    private JWTVerifier verifier;
    private final String Issuer = "Example.com";

    private void init() {
        algorithm = Algorithm.HMAC256("secret_key");

        verifier = JWT.require(algorithm)
                .withIssuer(Issuer)
                .build();
    }

    public String generateJwt(UserDetails userDetails) {
        String jwtToken = JWT.create()
                .withIssuer(Issuer)
                .withSubject("Details")
                .withClaim("email", userDetails.getUsername())
                .withClaim("roles", userDetails.getAuthorities().stream().map(ga-> ga.getAuthority()).collect(Collectors.joining(";")))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3_600_000L)) //1 hour
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
        return jwtToken;
    }

    public DecodedJWT validateJwtToken(String jwtToken, HttpServletResponse response) throws IOException {
        try {
            DecodedJWT decodedJWT = verifier.verify(jwtToken);
            return decodedJWT;
        } catch (JWTVerificationException ex) {
            log.warn("Token verification failure", ex);
            HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED.value(),
                            HttpStatus.UNAUTHORIZED,
                            ex.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            RestResponseUtil.sendHttpResponse(response, httpResponse);
        }
        return null;
    }
}
