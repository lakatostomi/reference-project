package com.example.caloriecalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@Jacksonized
@Builder
public class SportDTO {

    @NotBlank(message = "The field can not be empty!")
    private String nameOfActivity;
    @NotNull(message = "The field can not be null!")
    @Positive(message = "The field has to be positive!")
    private Integer user_id;
    @NotNull(message = "The field can not be null!")
    @Positive(message = "The field has to be positive!")
    private Double burned_calories;

    @Override
    public String toString() {
        return "SportDTO{" +
                "nameOfActivity='" + nameOfActivity + '\'' +
                ", user_id=" + user_id +
                ", burned_calories=" + burned_calories +
                '}';
    }
}


