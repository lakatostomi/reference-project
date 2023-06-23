package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.LoginRequestDTO;
import com.example.caloriecalculator.dto.RegistrationDTO;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.registration.RegistrationFinishedEvent;
import com.example.caloriecalculator.security.Auth0JwtUtils;
import com.example.caloriecalculator.service.interfaces.IAuthService;
import com.example.caloriecalculator.service.interfaces.IUserService;
import com.example.caloriecalculator.service.interfaces.IVerificationTokenService;
import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.util.RestResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rest/auth")
@Slf4j
@Tag(name = "AuthController", description = "Handles endpoints that are connected with authentication process!")
public class AuthController {

    private IAuthService authService;
    private IUserService userService;
    private IVerificationTokenService tokenService;
    private ApplicationEventPublisher eventPublisher;
    private UserModelAssembler modelAssembler;
    private AuthenticationManager authenticationManager;
    private Auth0JwtUtils jwtUtils;

    @Operation(summary = "Handles the registration process")
    @ApiResponses(value = {@ApiResponse(responseCode = "202", description = "1st step of registration is finished",
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Field/Object validation fails, Email already exists, Request body is missing"
                    , content = @Content(mediaType = "application/problem+json"))})
    @PostMapping(value = "/registration", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<Object> registerUser(@Parameter(description = "Contains the necessary fields for registration")
                                               @Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) {
        log.info("New registration process with: " + registrationDTO.toString());
        String response = "";
        if (!authService.checkEmailExists(registrationDTO.getEmail())) {
            User user = authService.registerUser(registrationDTO);
            eventPublisher.publishEvent(new RegistrationFinishedEvent(request.getRequestURL().toString(), user));

            response = RestResponseUtil.createJsonStringResponse(new HttpResponse(
                    HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED,
                    "Your registration is in progress, check your email account to verify the registration!"
            ));
            log.info(response);
        }
        return ResponseEntity.accepted().body(response);
    }

    @Operation(summary = "Handles the confirmation process of the registration")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Registration has confirmed",
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Token is not valid, Request param is missing"
                    , content = @Content(mediaType = "application/problem+json"))})
    @GetMapping(value = "/registration/confirm", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<Object> confirmRegistration(@Parameter(description = "Contains the token that previously has sent through email")
                                                      @RequestParam("token") String token) {
        VerificationToken verificationToken = tokenService.verifyToken(token);
        authService.enableUser(verificationToken.getUser());

        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(
                HttpStatus.CREATED.value(), HttpStatus.CREATED,
                "Your registration successfully finished! Please log in to continue!"
        ));
        log.info(response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Handles the login process")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "When login is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "Field validation fails, Request body is missing"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Authentication fails"
                    , content = @Content(mediaType = "application/problem+json"))})
    @PostMapping(value = "/login")
    public ResponseEntity<EntityModel<User>> login(@Parameter(description = "Contains the necessary fields for login")
                                                   @Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        log.info("New login process: " + loginRequestDTO);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        User user = userService.loginUser(loginRequestDTO.getEmail());
        String jwtToken = jwtUtils.generateJwt((UserDetails) authenticate.getPrincipal());
        response.addHeader("Authorization", "Bearer " + jwtToken);
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }
}
