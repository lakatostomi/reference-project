package com.example.caloriecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

@Configuration
public class CalorieCalculatorApplicationTests {

    @Bean
    @ServiceConnection
    @RestartScope
    MySQLContainer mySQLContainer() {
        return new MySQLContainer<>("mysql:8.0.30")
                .withInitScript("schema.sql");
    }

    public static void main(String[] args) {
        SpringApplication.from(CalorieCalculatorApplication::main)
                .with(CalorieCalculatorApplicationTests.class).run(args);
    }

}
