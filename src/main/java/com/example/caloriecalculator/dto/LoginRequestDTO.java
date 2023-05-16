package com.example.caloriecalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
@Getter
public class LoginRequestDTO {

    @Email(message = "This email format is not supported!")
    @NotBlank(message = "The field can not be empty!")
    private String email;

    @NotBlank(message = "The field can not be empty!")
    private String password;

    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "email='" + email + '\'' +
                ", password='" + "******" + '\'' +
                '}';
    }
}
