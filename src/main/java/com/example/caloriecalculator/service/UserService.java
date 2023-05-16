package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.ProfileUpdateDTO;
import com.example.caloriecalculator.exception.AccountIsUnavailableException;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.UserRepository;
import com.example.caloriecalculator.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class UserService implements IUserService {

    private UserRepository userRepository;

    @Override
    public User loginUser(String email) {
        User user = userRepository.findByEmail(email);
        if (!user.isEnabled()) {
            throw new AccountIsUnavailableException("Your account is not available! Confirm your registration before login!");
        }
        return user;
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("The user with [id=" + id + "] is not exist!"));
    }

    @Override
    public User updateProfileOfUser(ProfileUpdateDTO profileUpdateDTO, int id) {
        User user = findUserById(id);
        if (profileUpdateDTO.getAge() != null) {
            user.setAge(profileUpdateDTO.getAge());
        }
        if (profileUpdateDTO.getGender() != null) {
            user.setGender(profileUpdateDTO.getGender());
        }
        if (profileUpdateDTO.getHeight() != null) {
            user.setHeight(profileUpdateDTO.getHeight());
        }
        if (profileUpdateDTO.getActivity() != null) {
            user.setActivity(profileUpdateDTO.getActivity());
        }
        if (profileUpdateDTO.getWeight() != null) {
            user.setWeight(profileUpdateDTO.getWeight());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
