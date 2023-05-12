package com.example.caloriecalculator.dto;

import com.example.caloriecalculator.validation.PasswordMatches;
import com.example.caloriecalculator.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Jacksonized
@Getter
@AllArgsConstructor
@PasswordMatches
public class RegistrationDTO {

    @NotBlank(message = "The field can not be empty!")
    private String name;

    @Email
    @NotBlank(message = "The field can not be empty!")
    private String email;

    @ValidPassword
    @NotBlank(message = "The field can not be empty!")
    private String password;

    @NotBlank(message = "The field can not be empty!")
    private String confirmPassword;


    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "******" + '\'' +
                ", confirmPassword='" + "******" + '\'' +
                '}';
    }
}
