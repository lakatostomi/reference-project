package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class VerificationTokenRepositoryUnitTest {

    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(new User("Tom", "1234","tom@gmail.com", true));
        User user2 = userRepository.save(new User("Peter", "1234","peter@gmail.com", true));
        User user3 = userRepository.save(new User("Kate", "1234","kate@gmail.com", true));
        User user4 = userRepository.save(new User("John", "1234","john@gmail.com", true));
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.save(new VerificationToken("token", now.plusSeconds(1).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user1));
        tokenRepository.save(new VerificationToken("token2", now.minusSeconds(1).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user2));
        tokenRepository.save(new VerificationToken("token3", now.plusSeconds(10).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user3));
        tokenRepository.save(new VerificationToken("token4", now.minusSeconds(10).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user4));
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