package com.example.caloriecalculator.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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
                .withExpiresAt(new Date(System.currentTimeMillis() + 86_400_000L)) //1 day
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
        return jwtToken;
    }

    public DecodedJWT validateJwtToken(String jwtToken) throws JWTVerificationException {
        try {
            DecodedJWT decodedJWT = verifier.verify(jwtToken);
            return decodedJWT;
        } catch (JWTVerificationException ex) {
            log.error(ex.getMessage());
            throw new JWTVerificationException("Token can not be verified!");
        }
    }
}
