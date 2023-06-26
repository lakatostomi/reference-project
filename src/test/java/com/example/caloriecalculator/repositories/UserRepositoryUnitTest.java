package com.example.caloriecalculator.repositories;


import com.example.caloriecalculator.MyTestConfigClass;
import com.example.caloriecalculator.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Import(MyTestConfigClass.class)
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
}


