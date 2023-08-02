package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository  extends JpaRepository<Food, Integer> {
}
