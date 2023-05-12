package com.example.caloriecalculator.service.interfaces;

import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;

public interface ICalorieIntakeService {

    CalorieIntake findIntakeById(Integer id);

    User saveUsersCalorieIntake(IntakeDTO intakeDTO);

    User updateCalorieIntake(Integer intake_id, Double quantityOfIntake);

    void deleteCalorieIntake(Integer intake_id);
}
