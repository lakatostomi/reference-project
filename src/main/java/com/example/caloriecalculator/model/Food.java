package com.example.caloriecalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    //unit: kcal
    private double calories;
    //all nutrition values (calories, carbs, protein, fat) are calculated according to the serving size
    //unit: gram
    private int serving;
    //unit: gram
    private double carbs;
    //unit: gram
    private double protein;
    //unit: gram
    private double fat;
}
