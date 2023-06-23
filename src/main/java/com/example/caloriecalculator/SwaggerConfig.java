package com.example.caloriecalculator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.annotations.info.Info;

@Component
@OpenAPIDefinition(info = @Info(title = "Calorie Calculator REST API"
        , version = "0.0.1 SNAPSHOT"
        , description = """
                        This Calorie Calculator \"light\" REST API provides information about my actual skills through some basic functions.
                                                
                        The main functions in a few words:
                        - Create an account, confirm registration through a verification email, login
                        - Managing account (update profile, delete profile)
                        - Saving, updating, deleting calorie intakes, sport activities and foods
                                                
                        Some additional information:
                        - Auth0 authentication
                        - Introducing roles (ADMIN, USER)
                        - Delivering states through HATEOAS
                        - Resources saved in MYSQL DB
                        - Tests running in testcontainers (MySQL) (not every test class has developed - only 2 controller test, 1 service test, 2 repository test where queries have created with @Query annotation and 1 integration test
                        - Basic Dockerfile and docker-compose.yaml is configured
                        - In order to test that verification email is sent, set a valid host in application.properties
                        
                        Project is migrated to Spring Boot 3.1.0! Therefor some modifications were executed:
                        - H2 DB in dev environment has removed, instead of I use TestContainer, and I tested the docker compose support as well
                        so I created a compose.yaml which is recognised by the app after startup.
                        - Flyway migration removed I rely on Hibernate to create schema.
                        - I use problem+json format to handle errors therefore RestExceptionController class was recoded.
                                              
                        I permanently develop the API with further functions...
                        """,
        termsOfService = "http://example.com/terms/",
        license = @License(name = "Apache 2.0")
))
public class SwaggerConfig {
}
