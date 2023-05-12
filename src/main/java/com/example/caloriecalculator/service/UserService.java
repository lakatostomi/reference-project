package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.dto.ProfileUpdateDTO;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.FoodRepository;
import com.example.caloriecalculator.repositories.UserRepository;
import com.example.caloriecalculator.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class UserService implements IUserService {

    private UserRepository userRepository;

    @Override
    public User loginUser(String email) {
        return userRepository.findByEmail(email);
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
