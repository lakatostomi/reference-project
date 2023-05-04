package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.CalorieIntakeRepository;
import com.example.caloriecalculator.repositories.FoodRepository;
import com.example.caloriecalculator.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CalorieIntakeService {

    private CalorieIntakeRepository calorieIntakeRepository;
    private UserRepository userRepository;
    private FoodRepository foodRepository;

    public CalorieIntake findIntakeById(Integer id) {
        return calorieIntakeRepository.findById(id).orElseThrow(()-> new NoSuchElementException("This Intake is not exist!"));
    }

    public User saveUsersCalorieIntake(IntakeDTO intakeDTO) {
        User user = userRepository.findById(intakeDTO.getUserId()).orElseThrow(()-> new NoSuchElementException("The user is not exist!"));
        CalorieIntake calorieIntake = convertToCalorieIntake(intakeDTO, user);
        user.getCalorieIntakeList().add(calorieIntake);
        userRepository.save(user);
        return user;
    }

    private CalorieIntake convertToCalorieIntake(IntakeDTO source, User user) {
        CalorieIntake calorieIntake = new CalorieIntake();
        calorieIntake.setTimeOfIntake(new java.util.Date());
        calorieIntake.setQuantityOfFood(source.getQuantityOfFood());
        calorieIntake.setUser(user);
        calorieIntake.setFood(foodRepository.getReferenceById(source.getFoodId()));
        return calorieIntake;
    }

    public User updateCalorieIntake(Integer intake_id, Double quantityOfIntake) {
        CalorieIntake calorieIntake = calorieIntakeRepository.getReferenceById(intake_id);
        calorieIntake.setQuantityOfFood(quantityOfIntake);
        calorieIntakeRepository.save(calorieIntake);
        return userRepository.findById(calorieIntake.getUser().getId()).orElseThrow(()-> new NoSuchElementException("The user is not exist!"));
    }

    public void deleteIntake(Integer intake_id) {
        calorieIntakeRepository.deleteById(intake_id);
    }

}
