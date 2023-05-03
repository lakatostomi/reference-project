package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.RegistrationDTO;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.RoleRepository;
import com.example.caloriecalculator.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(RegistrationDTO registrationDTO) {
        User user = userRepository.save(new User(registrationDTO.getName(),
                encoder.encode(registrationDTO.getPassword()),
                registrationDTO.getEmail(),
                false));
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER")));
        return user;
    }

    public User enableUser(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
