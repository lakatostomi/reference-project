package com.example.caloriecalculator;


import com.example.caloriecalculator.model.Privilege;
import com.example.caloriecalculator.model.Role;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
@TestComponent
@Transactional
public abstract class CommonDataInitializer {

    @Autowired
    protected PasswordEncoder passwordEncoder;
    @PersistenceContext
    protected EntityManager entityManager;
    public CommonDataInitializer() {
    }

    public void initData() {
        Privilege writePrivilege = new Privilege("WRITE_PRIVILEGE");
        Privilege readPrivilege = new Privilege("READ_PRIVILEGE");
        Role roleUser = new Role("ROLE_USER", List.of(readPrivilege));
        Role roleAdmin = new Role("ROLE_ADMIN", List.of(readPrivilege, writePrivilege));
        entityManager.persist(readPrivilege);
        entityManager.persist(writePrivilege);
        entityManager.persist(roleUser);
        entityManager.persist(roleAdmin);
        User user1 = new User("Tom", passwordEncoder.encode("1234"),"tom@gmail.com", true);
        user1.setRoles(List.of(roleUser));
        User user2 = new User("Peter", passwordEncoder.encode("1234"), "peter@gmail.com", true);
        user2.setRoles(List.of(roleUser));
        User admin = new User("admin", passwordEncoder.encode("1234"), "admin@gmail.com", true);
        admin.setRoles(List.of(roleAdmin));
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(admin);
        LocalDateTime now = LocalDateTime.now();
        entityManager.persist(new VerificationToken("token", now.plusSeconds(1).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user1));
        entityManager.persist(new VerificationToken("token2", now.minusSeconds(1).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user2));
        entityManager.persist(new VerificationToken("token3", now.plusSeconds(10).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user2));
        entityManager.persist(new VerificationToken("token4", now.minusSeconds(10).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), user1));
        entityManager.flush();
        entityManager.clear();
    }

}
