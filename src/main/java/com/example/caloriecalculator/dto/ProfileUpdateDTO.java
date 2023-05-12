package com.example.caloriecalculator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProfileUpdateDTO {

    private Integer age;
    private String gender;
    private Double height;
    private Double weight;
    private Integer activity;
}
