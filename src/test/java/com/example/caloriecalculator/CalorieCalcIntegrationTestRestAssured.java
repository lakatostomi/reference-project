package com.example.caloriecalculator;

import com.example.caloriecalculator.dto.LoginRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
class CalorieCalcIntegrationTestRestAssured extends CommonDataInitializer{

    @LocalServerPort
    int port;

    private String url;

    @PostConstruct
    void initUrl() {url = "http://localhost:" + port + "/api/rest";}

    @BeforeEach
    void setUp() {
        initData();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testLogin_whenNotExistingAccount_thanForbidden() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("thisisnotexist@gmail.com", "1234");
        with().contentType("application/json").body(loginRequestDTO).when().post(url + "/auth/login").then()
                .statusCode(403).log().ifError().assertThat().body("message", equalTo("Bad credentials"));
    }

    @Test
    void testLogin_whenValidAccount_thanOK() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("tom@gmail.com", "1234");
        with().contentType("application/json").body(loginRequestDTO).when().post(url + "/auth/login").then()
                .statusCode(200).log().ifError()
                .assertThat().header("Authorization", isA(String.class))
                .assertThat().body("name", equalTo("Tom"));
    }
}