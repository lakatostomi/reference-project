package com.example.caloriecalculator.util;


import com.example.caloriecalculator.model.*;
import com.example.caloriecalculator.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Slf4j
public class CommonDataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private FoodRepository foodRepository;


    public CommonDataInitializer() {
    }

    public void initData() {
        log.info("Initializing dummy data has started!");
        Privilege writePrivilege = new Privilege("WRITE_PRIVILEGE");
        Privilege readPrivilege = new Privilege("READ_PRIVILEGE");
        Role roleUser = new Role("ROLE_USER", List.of(readPrivilege));
        Role roleAdmin = new Role("ROLE_ADMIN", List.of(readPrivilege, writePrivilege));
        privilegeRepository.save(readPrivilege);
        privilegeRepository.save(writePrivilege);
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        Food pizza = foodRepository.save(new Food(null, "Pizza", 300.0, 100, 50.0, 21.1, 32.0));
        Food hamburger = foodRepository.save(new Food(null, "Hamburger", 222.0, 100, 55.0, 22.1, 62.0));
        Food beefsteak = foodRepository.save(new Food(null, "Beefsteak", 233.0, 100, 21.0, 41.1, 22.0));
        User tom = new User("Tom", passwordEncoder.encode("1111"), "tom@gmail.com", true);
        tom.setRoles(List.of(roleUser));
        tom = userRepository.save(tom);
        User peter = new User("Peter", passwordEncoder.encode("2222"), "peter@gmail.com", true);
        peter.setRoles(List.of(roleUser));
        userRepository.save(peter);
        User admin = new User("admin", passwordEncoder.encode("3333"), "admin@gmail.com", true);
        admin.setRoles(List.of(roleAdmin));
        userRepository.save(admin);
        tom.getCalorieIntakeList().add(new CalorieIntake(null, new Date(), 343.2, hamburger, tom));
        tom.getSportList().add(new Sport(null, "Run", tom, 456.0, new Date()));
        userRepository.save(tom);
        LocalDateTime now = LocalDateTime.now();
        verificationTokenRepository.save(new VerificationToken("e5e38bed-dafe-4af2-a988-015424e69954", now.minusSeconds(60).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), tom));
        verificationTokenRepository.save(new VerificationToken("e5e38bed-dafe-4af2-a988-015424e68888", now.plusSeconds(10).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), peter));
        verificationTokenRepository.save(new VerificationToken("e6e38bed-dafe-4af2-a988-015424e69954", now.minusHours(25).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), peter));
        verificationTokenRepository.save(new VerificationToken("e5e38bed-dade-4af2-a988-015424e69954", now.plusSeconds(60).toInstant(ZoneOffset.of("+02:00")).toEpochMilli(), tom));
        log.info("Init data has finished!");
    }

    @PostConstruct
    public void initEm() {
        log.info(getClass().getName() + " Bean has injected!");
    }

}
