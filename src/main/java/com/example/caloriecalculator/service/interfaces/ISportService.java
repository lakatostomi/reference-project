package com.example.caloriecalculator.service.interfaces;

import com.example.caloriecalculator.dto.SportDTO;
import com.example.caloriecalculator.model.Sport;
import com.example.caloriecalculator.model.User;

public interface ISportService {

    User saveUsersActivity(SportDTO sportDTO);

    User updateActivity(Integer sport_id, Double burned_calories);

    Sport findById(Integer id);

    void deleteActivity(Integer id);
}
