package com.example.caloriecalculator.service.interfaces;

import com.example.caloriecalculator.dto.SportDTO;
import com.example.caloriecalculator.model.Sport;
import com.example.caloriecalculator.model.User;

public interface ISportService {

    User saveUsersSport(SportDTO sportDTO);

    User updateSport(Integer sport_id, Double burned_calories);

    Sport findSportById(Integer id);

    void deleteSport(Integer id);
}
