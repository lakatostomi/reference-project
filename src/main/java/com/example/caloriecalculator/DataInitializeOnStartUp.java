package com.example.caloriecalculator;

import com.example.caloriecalculator.model.*;
import com.example.caloriecalculator.repositories.FoodRepository;
import com.example.caloriecalculator.repositories.PrivilegeRepository;
import com.example.caloriecalculator.repositories.RoleRepository;
import com.example.caloriecalculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.websocket.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@Profile("dev")
public class DataInitializeOnStartUp implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Food pizza = saveFoods(new Food(null, "Pizza", 300.0, 100, 50.0, 21.1, 32.0));
        Food hamburger = saveFoods(new Food(null, "Hamburger", 222.0, 100, 55.0, 22.1, 62.0));
        Food beefsteak = saveFoods(new Food(null, "Beefsteak", 233.0, 100, 21.0, 41.1, 22.0));

        Privilege readPrivilege = createPrivilege("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilege("WRITE_PRIVILEGE");

        Role adminRole = new Role("ROLE_ADMIN", List.of(readPrivilege, writePrivilege));
        Role userRole = new Role("ROLE_USER", List.of(readPrivilege));
        saveRoles(adminRole);
        saveRoles(userRole);

        User admin = new User("admin", passwordEncoder.encode("admin"), "admin@admin.com", true);
        User tomi = new User("Tomi", passwordEncoder.encode("lt18I##IC"), "tomi@gmail.com", true);
        CalorieIntake intake = new CalorieIntake(null, new Date(), 300.0, pizza, tomi);
        CalorieIntake intake2 = new CalorieIntake(null, new Date(), 200.0, hamburger, tomi);
        Date date = Date.from(Instant.parse("2023-05-03T10:15:30.00Z"));
        CalorieIntake intake3 = new CalorieIntake(null, date, 100.0, beefsteak, tomi);
        tomi.setCalorieIntakeList(List.of(intake, intake2, intake3));
        admin.setRoles(List.of(adminRole));
        tomi.setRoles(List.of(userRole));
        saveUser(admin);
        saveUser(tomi);

    }

    @Transactional
    Privilege createPrivilege(String name) {
        return privilegeRepository.save(new Privilege(name));
    }

    @Transactional
    Role saveRoles(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    Food saveFoods(Food food) {
        return foodRepository.save(food);
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private void initSchema() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stm = conn.prepareStatement("CREATE SCHEMA \"CALORIECALCULATOR\";");) {
             stm.execute();
        } catch (SQLException ex) {
            throw  new RuntimeException(ex.getMessage());
        }
    }
}
