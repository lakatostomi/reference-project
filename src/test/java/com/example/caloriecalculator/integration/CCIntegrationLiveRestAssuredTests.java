package com.example.caloriecalculator.integration;

import com.example.caloriecalculator.MyTestContainer;
import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.dto.LoginRequestDTO;
import com.example.caloriecalculator.dto.RegistrationDTO;
import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.repositories.UserRepository;
import com.example.caloriecalculator.util.CommonDataInitializer;
import io.restassured.authentication.OAuthSignature;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CCIntegrationLiveRestAssuredTests {

    @LocalServerPort
    int port;

    private String url;

    @PostConstruct
    void initUrl() {
        url = "http://localhost:" + port + "/api/rest";
    }

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testRegistration_whenPasswordNotMatches_thanBadRequest() {
        RegistrationDTO registrationDTO = new RegistrationDTO("John", "john@john.com", "jhRR12&&", "jhRR12&&&");
        with().contentType("application/json").body(registrationDTO).when().post(url + "/auth/registration")
                .then().statusCode(400).log().ifError()
                .assertThat().body("detail", equalTo("Passwords are not matches!"));
    }

    @Test
    void testRegistration_whenPasswordAreNotValid_thanBadRequest() { //too short (at least 8 character) and doesn't contain any special character
        RegistrationDTO registrationDTO = new RegistrationDTO("John", "john@john.com", "jhRR12", "jhRR12");
        with().contentType("application/json").body(registrationDTO).when().post(url + "/auth/registration")
                .then().statusCode(400).log().ifError()
                .assertThat().body("detail", equalTo("password field: [Password must be at least 8 characters in length., Password must contain at least 2 special characters.],"));
    }

    @Test
    void testRegistration_whenRegFormOK_thanAccepted() {
        RegistrationDTO registrationDTO = new RegistrationDTO("John", "john@john.com", "jhRR12$$", "jhRR12$$");
        with().contentType("application/json").body(registrationDTO).when().post(url + "/auth/registration")
                .then().statusCode(202).log().ifError()
                .assertThat().body("message", equalTo("Your registration is in progress, check your email account to verify the registration!"));
    }

    @Test
    void testRegistration_whenEmailAlreadyExist_thanBadRequest() {
        RegistrationDTO registrationDTO = new RegistrationDTO("Tom", "tom@gmail.com", "jhRR12$$", "jhRR12$$");
        with().contentType("application/json").body(registrationDTO).when().post(url + "/auth/registration")
                .then().statusCode(400).log().ifError()
                .assertThat().body("detail", equalTo("An account for that email already exists: [" + registrationDTO.getEmail() + "]!"));
    }

    @Test
    void testConfirmReg_whenTokenValid_thanCreated() {
        String token = "e5e38bed-dafe-4af2-a988-015424e69954";
        with().param("token", token).when().get(url + "/auth/registration/confirm").then().statusCode(201)
                .log().ifError()
                .assertThat().body("message", equalTo("Your registration successfully finished! Please log in to continue!"));
    }

    @Test
    void testConfirmReg_whenTokenNotValid_thanBadRequest() {
        String token = "thisisnotavalidtoken";
        with().param("token", token).when().get(url + "/auth/registration/confirm").then().statusCode(400)
                .log().ifError().assertThat().body("detail", equalTo("The token is not exist!"));
    }

    @Test
    void testConfirmReg_whenTokenExpired_thanBadRequest() {
        String token = "e6e38bed-dafe-4af2-a988-015424e69954";
        with().param("token", token).when().get(url + "/auth/registration/confirm").then().statusCode(400)
                .log().ifError().assertThat().body("detail", equalTo("Token has expired!"));
    }

    @Test
    void testLogin_whenNotExistingAccount_thanUnauthorized() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("thismailnotexist@gmail.com", "1234");
        with().contentType("application/json").body(loginRequestDTO).when().post(url + "/auth/login").then()
                .statusCode(401).log().ifError().assertThat().body("detail", equalTo("Bad credentials"));
    }

    @Test
    void testLogin_whenValidAccount_thanOK() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("tom@gmail.com", "1111");
        with().contentType("application/json").body(loginRequestDTO).when().post(url + "/auth/login").then()
                .statusCode(200).log().ifError()
                .assertThat().header("Authorization", isA(String.class))
                .assertThat().body("name", equalTo("Tom"));
    }

    @Test
    void testRequestMethodNotSupported_whenCallAnEndpointThatNotExists_thanMethodNotAllowed() {
        //login with an existing account to get bearer token for authorization
        String token = executeLoginToGetToken("admin@gmail.com", "3333");

        given().auth().oauth2(token, OAuthSignature.HEADER).when().post(url + "/user/savesport").then().statusCode(405);
    }


    @Test
    void testUserRole_whenUserInvokeRestrictedEndpoint_thanForbidden() {
        String token = executeLoginToGetToken("tom@gmail.com", "1111");

        //list all users
        given().auth().oauth2(token, OAuthSignature.HEADER).when().get(url + "/user")
                .then().statusCode(403).log().ifError()
                .body("detail", equalTo("You do not have permission to access this page!"));

        //save new food
        Food pizza = new Food(null, "Pizza", 300.0, 100, 50.0, 21.1, 32.0);
        given().auth().oauth2(token, OAuthSignature.HEADER).with().contentType("application/json").body(pizza).when().post(url + "/foods")
                .then().statusCode(403).log().ifError()
                .body("detail", equalTo("You do not have permission to access this page!"));
    }

    @Test
    void testLoginWithAdmin_whenAdminInvokeRestrictedEndpoint_thanOK() {
        String token = executeLoginToGetToken("admin@gmail.com", "3333");

        given().auth().oauth2(token, OAuthSignature.HEADER).when().get(url + "/user")
                .then().statusCode(200).log().ifError()
                .body("_embedded.userList.size()", is(3))
                .assertThat().body("_embedded.userList[0].name", equalTo("Tom"))
                .assertThat().body("_embedded.userList[1]._links.self.href", equalTo(url + "/user/2"))
                .assertThat().body("_embedded.userList[2].email", equalTo("admin@gmail.com"));
    }

    @Test
    void testSaveCalorieIntake_whenFieldsAreInvalid_thanBadRequest() {
        String token = executeLoginToGetToken("tom@gmail.com", "1111");

        IntakeDTO intakeDTO = new IntakeDTO(0, -1, null);
        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json")
                .body(intakeDTO).post(url + "/user/intake").then()
                .statusCode(400).log().ifError()
                .body("detail", containsString("foodId field: The value of the field has to be positive!"))
                .body("detail", containsString("userId field: The value of the field has to be positive!"))
                .body("detail", containsString("quantityOfFood field: The field can not be null!"));
    }

    @Test
    void testSaveCalorieIntake_whenFieldsValid_thanCreated() {
        String token = executeLoginToGetToken("tom@gmail.com", "1111");

        IntakeDTO intakeDTO = new IntakeDTO(1, 1, 500.0);
        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json")
                .body(intakeDTO).post(url + "/user/intake").then()
                .statusCode(200).log().ifError()
                .body("_links.allIntake[1].href", equalTo(url + "/user/intake/3"));

        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json")
                .get(url + "/user/intake/{id}", 3).then().statusCode(200)
                .log().ifError()
                .assertThat().body("quantityOfFood", equalTo(500.0F))
                .assertThat().body("food.name", equalTo("Pizza"))
                .assertThat().body("user.name", equalTo("Tom")).extract().response().body().print();
    }

    @Test
    void testUpdateIntake_whenQuantityIsNotPositive_thanException() {
        String token = executeLoginToGetToken("tom@gmail.com", "1111");

        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json").queryParams("id", "1", "quantity", "0")
                .put(url + "/user/intake").then().statusCode(400)
                .log().ifError().body("detail", equalTo("The quantity has to be greater than 0!"));
    }

    @Test
    void testUpdateIntake_whenParamsOk_thanOk() {
        String token = executeLoginToGetToken("tom@gmail.com", "1111");

        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json").queryParams("id", "1", "quantity", "100.0")
                .put(url + "/user/intake").then().statusCode(200).log().ifError();

        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json")
                .get(url + "/user/intake/{id}", 1).then().statusCode(200)
                .log().ifError()
                .assertThat().body("quantityOfFood", equalTo(100.0F));
    }

    @Test
    void testDeleteIntake_whenIdOk_thanNoContent() {
        String token = executeLoginToGetToken("tom@gmail.com", "1111");

        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json").pathParam("id", "1")
                .delete(url + "/user/intake/{id}").then().statusCode(204).log().ifError();
    }

    @Test
    void testDeleteIntake_whenIdNotExists_thanBadRequests() {
        String token = executeLoginToGetToken("tom@gmail.com", "1111");

        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json").pathParam("id", "14")
                .delete(url + "/user/intake/{id}").then().statusCode(400).log().ifError()
                .assertThat().body("detail", equalTo("This calorie intake with id=14 is not exist!"));
    }

    @Test
    void testOrphanRemoval_whenDeleteUser_thanRemoveIntakesAndSports() {
        String token = executeLoginToGetToken("peter@gmail.com", "2222");
        //delete user account
        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json").pathParam("id", "2")
                .delete(url + "/user/{id}").then().statusCode(204).log().ifError();
        //send request to get an intake of user
        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json").pathParam("id", "2")
                .get(url + "/user/intake/{id}").then().statusCode(400).log().ifError()
                .assertThat().body("detail", equalTo("This calorie intake with id=2 is not exist!"));
        //send request to get a sport activity of user
        given().auth().oauth2(token, OAuthSignature.HEADER)
                .with().contentType("application/json").pathParam("id", "2")
                .get(url + "/user/sport/{id}").then().statusCode(400).log().ifError()
                .assertThat().body("detail", equalTo("This sport with id=2 is not exist!"));
    }

    private String executeLoginToGetToken(String email, String password) {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        return with().contentType("application/json").body(loginRequestDTO).when().post(url + "/auth/login")
                .then().statusCode(200)
                .log().ifError().extract().header("Authorization").substring("Bearer ".length());
    }

    @TestConfiguration
    @ImportTestcontainers(MyTestContainer.class)
    static class TestConfig {

        @Transactional
        @Bean(initMethod = "initData")
        public CommonDataInitializer commonDataInitializer() {
            return new CommonDataInitializer();
        }
    }
}