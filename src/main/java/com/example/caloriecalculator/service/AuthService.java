package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.RegistrationDTO;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.RoleRepository;
import com.example.caloriecalculator.repositories.UserRepository;
import com.example.caloriecalculator.service.interfaces.IAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class AuthService implements IAuthService {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User registerUser(RegistrationDTO registrationDTO) {
        User user = userRepository.save(new User(registrationDTO.getName(),
                encoder.encode(registrationDTO.getPassword()),
                registrationDTO.getEmail(),
                false));
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER")));
        log.debug("User saved: " + user.toString());
        return user;
    }

    @Override
    public User enableUser(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
