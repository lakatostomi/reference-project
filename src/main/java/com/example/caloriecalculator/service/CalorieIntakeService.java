package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.CalorieIntakeRepository;
import com.example.caloriecalculator.repositories.FoodRepository;
import com.example.caloriecalculator.repositories.UserRepository;
import com.example.caloriecalculator.service.interfaces.ICalorieIntakeService;
import com.mysql.cj.exceptions.NumberOutOfRange;
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
        log.info("User={} is request a calorie intake with={}", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        return calorieIntakeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This calorie intake with id=" + id + " is not exist!"));
    }

    @Override
    public User saveUsersCalorieIntake(IntakeDTO intakeDTO) {
        log.info("User={} is saving a new calorie intake={} ", SecurityContextHolder.getContext().getAuthentication().getName(), intakeDTO);
        User user = getUser(intakeDTO.getUserId());
        CalorieIntake calorieIntake = convertToCalorieIntake(intakeDTO, user);
        user.getCalorieIntakeList().add(calorieIntake);
        userRepository.save(user);
        return user;
    }

    private User getUser(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This user with id=" + id + " is not exist!"));
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
        log.info("User={} is updating a calorie intake id={} new quantity={}!", SecurityContextHolder.getContext().getAuthentication().getName(), intake_id, quantityOfIntake);
        CalorieIntake calorieIntake = calorieIntakeRepository.getReferenceById(intake_id);
        if (quantityOfIntake <= 0) {
            throw new NumberOutOfRange("The quantity has to be greater than 0!");
        }
        calorieIntake.setQuantityOfFood(quantityOfIntake);
        calorieIntakeRepository.save(calorieIntake);
        return getUser(calorieIntake.getUser().getId());
    }

    @Override
    public void deleteCalorieIntake(Integer intake_id) {
        log.info("User={} is deleting a calorie intake id={}", SecurityContextHolder.getContext().getAuthentication().getName(), intake_id);
        calorieIntakeRepository.deleteById(intake_id);
    }

}
