package com.example.caloriecalculator.repositories;


import com.example.caloriecalculator.util.CommonDataInitializer;
import com.example.caloriecalculator.model.User;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Profile("test")
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindUserAndFetchRoles() {
        User user = userRepository.findByEmailAndFetchRoles("tom@gmail.com");
        assertEquals(1, user.getRoles().size());
    }

    @Test
    public void testFindUserWithoutFetch() {
        User user = userRepository.findByEmail("admin@gmail.com");
        assertThrows(LazyInitializationException.class, () -> user.getRoles().size());
    }

    @AfterEach
    void after() {

    }

    @TestConfiguration
    public static class TestConfigClass {

        @Transactional
        @Bean(initMethod = "initData")
        public CommonDataInitializer commonDataInitializer() {
            return new CommonDataInitializer();
        }
    }
}


