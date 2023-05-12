package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.service.interfaces.ICalorieIntakeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/rest/user/intake")
@AllArgsConstructor
@Slf4j
public class CalorieIntakeController {

    private ICalorieIntakeService intakeService;
    private UserModelAssembler modelAssembler;


    @PostMapping
    public ResponseEntity<EntityModel<User>> saveNewCalorieIntake(@RequestBody @Valid IntakeDTO intakeDTO) {
        User user = intakeService.saveUsersCalorieIntake(intakeDTO);
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }

    @PutMapping("/{id}&{quantity}")
    public ResponseEntity<EntityModel<User>> updateCalorieIntake(@PathVariable Integer id, @PathVariable Double quantity) {
        User user = intakeService.updateCalorieIntake(id, quantity);
        return ResponseEntity.ok(modelAssembler.toModel(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalorieIntake> findIntakeById(@PathVariable Integer id) {
        return ResponseEntity.ok(intakeService.findIntakeById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalorieIntake(@PathVariable Integer id) {
        intakeService.deleteCalorieIntake(id);
        return ResponseEntity.noContent().build();
    }
}
