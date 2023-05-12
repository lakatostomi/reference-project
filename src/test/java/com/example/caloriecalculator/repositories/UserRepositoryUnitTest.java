package com.example.caloriecalculator.repositories;


import com.example.caloriecalculator.CommonDataInitializer;
import com.example.caloriecalculator.model.User;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserRepositoryUnitTest extends CommonDataInitializer {

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setup() {
        initData();
    }

    @Test
    public void testFindUserAndFetchRoles() {
        User user = userRepository.findByEmailAndFetchRoles("tom@gmail.com");
        assertEquals(1, user.getRoles().size());
    }

    @Test
    public void testFindUserWithoutFetch() {
        User user = userRepository.findByEmail("admin@gmail.com");
        assertThrows(LazyInitializationException.class, ()->user.getRoles().size());
    }

    @AfterEach
    void after() {

    }
}


