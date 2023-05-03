package com.example.caloriecalculator;

import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CalorieCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalorieCalculatorApplication.class, args);
    }

}
