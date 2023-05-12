package com.example.caloriecalculator.service.interfaces;

import com.example.caloriecalculator.dto.FoodDTO;
import com.example.caloriecalculator.model.Food;

import java.util.List;

public interface IFoodService {

    List<Food> findAll();
    Food findFoodById(Integer id);
    Food saveFood(FoodDTO foodDTO);
    Food updateFood(FoodDTO foodDTO, Integer id);
    void deleteFood(Integer id);
}
