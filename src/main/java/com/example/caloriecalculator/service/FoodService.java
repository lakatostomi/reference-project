package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.FoodDTO;
import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.repositories.FoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class FoodService implements Converter<FoodDTO, Food> {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    public Food findById(Integer id) throws NoSuchElementException{
        return foodRepository.findById(id).orElseThrow(()-> new NoSuchElementException("The given food is not exist!"));
    }

    public Food save(FoodDTO foodDTO) {
        return foodRepository.save(convert(foodDTO));
    }

    public Food updateFood(FoodDTO foodDTO, Integer id) {
        Food food = convert(foodDTO);
        food.setId(id);
        return foodRepository.save(food);
    }

    public void delete(Integer id) {
        foodRepository.deleteById(id);
    }

    @Override
    public Food convert(FoodDTO source) {
        return new Food(null, source.getName(), source.getCalories(), source.getServing(), source.getCarbs(), source.getProtein(), source.getFat());
    }
}
