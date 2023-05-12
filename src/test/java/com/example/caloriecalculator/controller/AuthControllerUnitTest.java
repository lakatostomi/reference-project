package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.LoginRequestDTO;
import com.example.caloriecalculator.dto.RegistrationDTO;
import com.example.caloriecalculator.exception.TokenHasExpiredException;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.VerificationToken;
import com.example.caloriecalculator.registration.RegistrationFinishedEvent;
import com.example.caloriecalculator.registration.RegistrationListener;
import com.example.caloriecalculator.security.Auth0JwtUtils;
import com.example.caloriecalculator.service.AuthService;
import com.example.caloriecalculator.service.UserService;
import com.example.caloriecalculator.service.VerificationTokenService;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Profile("test")
@AutoConfigureMockMvc
class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;
    @MockBean
    private Auth0JwtUtils jwtUtils;
    @MockBean
    private AuthenticationManager manager;
    @MockBean
    private ApplicationEventPublisher publisher;
    @MockBean
    private VerificationTokenService tokenService;
    @MockBean
    private RegistrationListener listener;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testRegistration() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("Test", "test@test.com", "LT18i8##o","LT18i8##o");
        User user = Instancio.create(User.class);
        when(authService.checkEmailExists(registrationDTO.getEmail())).thenReturn(false);
        when(authService.registerUser(registrationDTO)).thenReturn(user);
        ArgumentCaptor<RegistrationFinishedEvent> finishedEventArgumentCaptor = ArgumentCaptor.forClass(RegistrationFinishedEvent.class);
        doAnswer(invocationOnMock -> {
            RegistrationFinishedEvent event = new RegistrationFinishedEvent("url", user);
           return event;
        }).when(publisher).publishEvent(finishedEventArgumentCaptor.capture());
        doNothing().when(listener).onApplicationEvent(any(RegistrationFinishedEvent.class));
        MvcResult result = mockMvc.perform(post("/api/rest/auth/registration").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registrationDTO))).andExpect(status().isAccepted()).andReturn();
        verify(publisher, times(1)).publishEvent(any(RegistrationFinishedEvent.class));
        verify(authService, times(1)).checkEmailExists(registrationDTO.getEmail());
        verify(authService, times(1)).registerUser(registrationDTO);
        assertThat(result.getResponse().getContentAsString(), containsString("Your registration is in progress, check your email account to finish the registration!"));

    }

    @Test
    void testRegistration_whenEmailExist_thanException() throws Exception{
        RegistrationDTO registrationDTO = new RegistrationDTO("Test", "test@test.com", "LT18i8##o","LT18i8##o");
        when(authService.checkEmailExists(registrationDTO.getEmail())).thenReturn(true);
        MvcResult result = mockMvc.perform(post("/api/rest/auth/registration").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registrationDTO))).andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("An account for that email already exists: [" + registrationDTO.getEmail() + "]!"));
    }

    @Test //should test every field constraints, but now I test only one object constraint
    void whenPasswordNotMatches_thanException() throws Exception{
        RegistrationDTO registrationDTO = new RegistrationDTO("Test", "test@test.com", "LT18i8##o0","LT18i8##o");
        MvcResult result = mockMvc.perform(post("/api/rest/auth/registration").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registrationDTO))).andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("Passwords are not matches!"));
    }

    @Test
    void testConfirmRegistration() throws Exception {
        VerificationToken verificationToken = Instancio.create(VerificationToken.class);
        when(tokenService.verifyToken(any(String.class))).thenReturn(verificationToken);
        when(authService.enableUser(any(User.class))).thenReturn(verificationToken.getUser());
        MvcResult result = mockMvc.perform(get("/api/rest/auth/registration/confirm").param("token", "234edfdf"))
                .andExpect(status().isCreated()).andReturn();
        verify(tokenService, times(1)).verifyToken(any(String.class));
        verify(authService, times(1)).enableUser(any(User.class));
        assertThat(result.getResponse().getContentAsString(), containsString("Your registration successfully finished! Please log in to continue!"));
    }

    @Test
    void testConfirmRegistration_whenTokenHasExpired_thanException() throws Exception{
        when(tokenService.verifyToken(any(String.class))).thenThrow(TokenHasExpiredException.class);
        mockMvc.perform(get("/api/rest/auth/registration/confirm").param("token", "234edfdf"))
                .andExpect(status().isBadRequest()).andReturn();
        verify(tokenService, times(1)).verifyToken(any(String.class));
    }


    @Test
    void testLogin() throws Exception {
        User user = Instancio.create(User.class);
        LoginRequestDTO requestDTO = Instancio.create(LoginRequestDTO.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new org.springframework.security.core.userdetails.User(requestDTO.getEmail(), requestDTO.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        when(manager.authenticate((new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword())))).thenReturn(authentication);
        when(jwtUtils.generateJwt(any(UserDetails.class))).thenReturn("token");
        when(userService.loginUser(any(String.class))).thenReturn(user);
        MvcResult result = mockMvc.perform(post("/api/rest/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDTO))).andExpect(status().isOk()).andReturn();
        verify(jwtUtils, times(1)).generateJwt(any(UserDetails.class));
        verify(userService, times(1)).loginUser(any(String.class));
        String token = result.getResponse().getHeader("Authorization");
        assertEquals(token, "Bearer token");
        assertThat(result.getResponse().getContentAsString(), containsString(user.getName()));
    }

    @Test
    void testLogin_withNotExistingCredentials() throws Exception{
        LoginRequestDTO requestDTO = Instancio.create(LoginRequestDTO.class);
        when(manager.authenticate((new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()))))
                .thenThrow(UsernameNotFoundException.class);
        mockMvc.perform(post("/api/rest/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDTO))).andExpect(status().is4xxClientError());
    }

}