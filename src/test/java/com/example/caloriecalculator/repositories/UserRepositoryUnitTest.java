package com.example.caloriecalculator.repositories;


import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.util.CommonDataInitializer;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
//@Testcontainers
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    /*@Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:8.0.30")
            .withDatabaseName("testContainer")
            .withUsername("root")
            .withPassword("test")
            .withInitScript("schema.sql");

    @DynamicPropertySource
    private static void setupProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }*/

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

    @Test
    public void testFindUserByCalorieIntake() {
        User user = userRepository.findUserByCalorieIntake(1);
        assertEquals("Tom", user.getName());
    }

    @Test
    public void testFindUserBySport() {
        User user = userRepository.findUserBySport(1);
        assertEquals("Tom", user.getName());
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


