package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.FoodDTO;
import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.repositories.FoodRepository;
import com.example.caloriecalculator.service.interfaces.IFoodService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@AllArgsConstructor
public class FoodService implements Converter<FoodDTO, Food>, IFoodService {

    private final FoodRepository foodRepository;

    @Override
    public List<Food> findAll() {
        log.info("User={} requests all food stored in database", SecurityContextHolder.getContext().getAuthentication().getName());
        return foodRepository.findAll();
    }

    @Override
    public Food findFoodById(Integer id) {
        log.info("User={} requests foods with id={}", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        return foodRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This food with id=" + id + "is not exist!"));
    }

    @Override
    public Food saveFood(FoodDTO foodDTO) {
        log.info("User={} is saving a new food={}", SecurityContextHolder.getContext().getAuthentication().getName(), foodDTO);
        return foodRepository.save(convert(foodDTO));
    }

    @Override
    public Food updateFood(FoodDTO foodDTO, Integer id) {
        log.info("User={} is updating a food id={} with values={}",SecurityContextHolder.getContext().getAuthentication().getName(), id, foodDTO);
        if (id == null) {
            throw new NoSuchElementException("The id of food can not be null!");
        }
        Food food = convert(foodDTO);
        food.setId(id);
        return foodRepository.save(food);
    }

    @Override
    public void deleteFood(Integer id) {
        log.info("User={} is deleting a food with id={}", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        foodRepository.deleteById(id);
    }

    @Override
    public Food convert(FoodDTO source) {
        return new Food(null,
                source.getName(),
                source.getCalories(),
                source.getServing(),
                source.getCarbs(),
                source.getProtein(),
                source.getFat());
    }
}
