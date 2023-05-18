package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.CalorieIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CalorieIntakeRepository extends JpaRepository<CalorieIntake, Integer> {
}
