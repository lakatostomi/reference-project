package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.MyTestConfigClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@Import(MyTestConfigClass.class)
class VerificationTokenRepositoryUnitTest {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testScheduledMethod() {
        tokenRepository.deleteIfExpired(Timestamp.valueOf(LocalDateTime.now()));
        assertEquals(2L, tokenRepository.count());
    }
}