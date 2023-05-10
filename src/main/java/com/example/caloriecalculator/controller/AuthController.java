package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.LoginRequestDTO;
import com.example.caloriecalculator.dto.RegistrationDTO;
import com.example.caloriecalculator.exception.EmailAlreadyExistsException;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.registration.RegistrationFinishedEvent;
import com.example.caloriecalculator.security.Auth0JwtUtils;
import com.example.caloriecalculator.service.AuthService;
import com.example.caloriecalculator.service.UserService;
import com.example.caloriecalculator.service.VerificationTokenService;
import com.example.caloriecalculator.util.HttpResponse;
import com.example.caloriecalculator.util.RestResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rest/auth")
public class AuthController {

    private AuthService authService;
    private UserService userService;
    private VerificationTokenService tokenService;
    private ApplicationEventPublisher eventPublisher;
    private UserModelAssembler modelAssembler;
    private AuthenticationManager authenticationManager;
    private Auth0JwtUtils jwtUtils;



    @PostMapping(value = "/registration", produces = "application/json")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) {
        if (authService.checkEmailExists(registrationDTO.getEmail())) {
            throw new EmailAlreadyExistsException(registrationDTO.getEmail());
        }

        User user = authService.registerUser(registrationDTO);
        eventPublisher.publishEvent(new RegistrationFinishedEvent(request.getRequestURL().toString(), user));

        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(
                HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED,
                "Your registration is in progress, check your email account to finish the registration!"
        ));
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping(value = "/registration/confirm", produces = "application/json")
    public ResponseEntity<Object> confirmRegistration(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenService.verifyToken(token);
        authService.enableUser(verificationToken.getUser());

        String response = RestResponseUtil.createJsonStringResponse(new HttpResponse(
                HttpStatus.CREATED.value(), HttpStatus.CREATED,
                "Your registration successfully finished! Please log in to continue!"
        ));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<EntityModel<User>> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        String jwtToken = jwtUtils.generateJwt((UserDetails) authenticate.getPrincipal());
        response.addHeader("Authorization", "Bearer " + jwtToken);
        User user = userService.loginUser(loginRequestDTO.getEmail());
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }
}