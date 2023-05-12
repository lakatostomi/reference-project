package com.example.caloriecalculator.service.interfaces;

import com.example.caloriecalculator.dto.ProfileUpdateDTO;
import com.example.caloriecalculator.model.User;

import java.util.List;

public interface IUserService {

    List<User> findAll();

    User loginUser(String email);

    User findUserById(int id);

    User updateProfileOfUser(ProfileUpdateDTO profileUpdateDTO, int id);

    void deleteUser(int id);
}
