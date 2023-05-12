package com.example.caloriecalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {

    @NotBlank(message = "The field can not be empty!")
    private String name;

    @NotNull(message = "The field can not be empty!")
    @PositiveOrZero(message = "The field has to be zero or positive!")
    private Double calories;

    @NotNull(message = "The field can not be empty!")
    @PositiveOrZero(message = "The field has to be zero or positive!")
    private Integer serving;

    @NotNull(message = "The field can not be empty!")
    @PositiveOrZero(message = "The field has to be zero or positive!")
    private Double carbs;

    @NotNull(message = "The field can not be empty!")
    @PositiveOrZero(message = "The field has to be zero or positive!")
    private Double protein;

    @NotNull(message = "The field can not be empty!")
    @PositiveOrZero(message = "The field has to be zero or positive!")
    private Double fat;

    @Override
    public String toString() {
        return "FoodDTO{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", serving=" + serving +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", fat=" + fat +
                '}';
    }
}
