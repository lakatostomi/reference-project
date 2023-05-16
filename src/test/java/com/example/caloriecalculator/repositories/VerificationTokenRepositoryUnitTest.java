package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.util.CommonDataInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Profile("test")
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

    @TestConfiguration
    static class TestConfigClass {

        @Transactional
        @Bean(initMethod = "initData")
        public CommonDataInitializer commonDataInitializer() {
            return new CommonDataInitializer();
        }
    }
}