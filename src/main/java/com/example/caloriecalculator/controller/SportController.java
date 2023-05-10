package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.SportDTO;
import com.example.caloriecalculator.model.Sport;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.service.SportService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rest/user/sport")
public class SportController {

    private UserModelAssembler modelAssembler;
    private SportService sportService;

    @PostMapping
    public ResponseEntity<EntityModel<User>> saveSportActivity(@RequestBody @Valid SportDTO sportDTO) {
        User user = sportService.saveUsersActivity(sportDTO);
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sport> findSportById(@PathVariable Integer id) {
        return new ResponseEntity<>(sportService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}&{calories}")
    public ResponseEntity<EntityModel<User>> updateSportActivity(@PathVariable Integer id, @PathVariable Double calories) {
        User user = sportService.updateActivity(id, calories);
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSportActivity(@PathVariable Integer id) {
        sportService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
