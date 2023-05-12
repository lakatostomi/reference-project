package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.CalorieIntakeRepository;
import com.example.caloriecalculator.repositories.FoodRepository;
import com.example.caloriecalculator.repositories.UserRepository;
import com.example.caloriecalculator.service.interfaces.ICalorieIntakeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class CalorieIntakeService implements ICalorieIntakeService {

    private CalorieIntakeRepository calorieIntakeRepository;
    private UserRepository userRepository;
    private FoodRepository foodRepository;

    @Override
    public CalorieIntake findIntakeById(Integer id) {
        log.info("User={} is request a calorie intake with={}",SecurityContextHolder.getContext().getAuthentication().getName(), id);
        return calorieIntakeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This calorie intake with id=" + id +  " is not exist!"));
    }

    @Override
    public User saveUsersCalorieIntake(IntakeDTO intakeDTO) {
        log.info("User={} is saving a new calorie intake={} ", SecurityContextHolder.getContext().getAuthentication().getName(), intakeDTO);
        User user = userRepository.findById(intakeDTO.getUserId()).orElseThrow(() -> new NoSuchElementException("This user with id=" + intakeDTO.getUserId() + " is not exist!"));
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

    @Override
    public User updateCalorieIntake(Integer intake_id, Double quantityOfIntake) {
        log.info("User={} is updating a calorie intake id={} new quantity={}!",SecurityContextHolder.getContext().getAuthentication().getName(), intake_id, quantityOfIntake);
        CalorieIntake calorieIntake = calorieIntakeRepository.getReferenceById(intake_id);
        calorieIntake.setQuantityOfFood(quantityOfIntake);
        calorieIntakeRepository.save(calorieIntake);
        return userRepository.findById(calorieIntake.getUser().getId()).orElseThrow(() -> new NoSuchElementException("This user with id=" + calorieIntake.getUser().getId() + " is not exist!"));
    }

    @Override
    public void deleteCalorieIntake(Integer intake_id) {
        log.info("User={} is deleting calorie intake id={}", SecurityContextHolder.getContext().getAuthentication().getName(), intake_id);
        calorieIntakeRepository.deleteById(intake_id);
    }

}
