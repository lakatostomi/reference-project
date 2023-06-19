package com.example.caloriecalculator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class CalorieCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalorieCalculatorApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Calorie Calculator REST API")
                .version("0.0.1 SNAPSHOT")
                .description("""
                        This Calorie Calculator \"light\" REST API provides information about my actual skills through some basic functions.
                                                
                        The main functions in a few words:
                        - Create an account, confirm registration through a verification email, login
                        - Managing account (update profile, delete profile)
                        - Saving, updating, deleting calorie intakes, sport activities and foods
                                                
                        Some additional information:
                        - Auth0 authentication
                        - Introducing roles (ADMIN, USER)
                        - Delivering states through HATEOAS
                        - Tests running in testcontainers (MySQL) (not every test class has developed - only 2 controller test, 2 service test, 2 repository test where queries have created with @Query annotation and 1 integration test)
                        - H2 DB in dev environment and MySQL DB in prod environment
                        - In prod environment DB is migrated by Flyway
                        - Basic Dockerfile and docker-compose.yaml is configured
                        - In order to test that verification email is sent, set a valid host in application.properties
                                                
                        I permanently develop the API with further functions...
                        """)
                .termsOfService("http://example.com/terms/")
                .license(new License().name("Apache 2.0")));
    }
}
