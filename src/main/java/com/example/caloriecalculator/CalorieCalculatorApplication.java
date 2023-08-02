package com.example.caloriecalculator;


import com.example.caloriecalculator.model.Role;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
public class CalorieCalculatorApplication implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(CalorieCalculatorApplication.class, args);
    }

    @Transactional
    @Profile("prod")
    @Override
    public void run(String... args) throws Exception {
        User admin = new User("admin", passwordEncoder.encode("admin"), "admin@admin.com", true);
        Role adminRole = entityManager.getReference(Role.class, 2);
        admin.setRoles(List.of(adminRole));
        entityManager.persist(admin);
        entityManager.flush();
    }
}
