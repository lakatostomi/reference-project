package com.example.caloriecalculator;


import com.example.caloriecalculator.model.Role;
import com.example.caloriecalculator.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("prod")
public class DataInitProdEnv implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        User admin = new User("admin", passwordEncoder.encode("admin"), "admin@admin.com", true);
        Role adminRole = entityManager.getReference(Role.class, 2);
        admin.setRoles(List.of(adminRole));
        entityManager.persist(admin);
        entityManager.flush();
    }
}
