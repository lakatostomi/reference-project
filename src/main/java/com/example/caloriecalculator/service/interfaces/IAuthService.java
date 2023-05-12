package com.example.caloriecalculator.service.interfaces;

import com.example.caloriecalculator.dto.RegistrationDTO;
import com.example.caloriecalculator.model.User;

public interface IAuthService {

    boolean checkEmailExists(String email);

    User registerUser(RegistrationDTO registrationDTO);

    User enableUser(User user);

}
