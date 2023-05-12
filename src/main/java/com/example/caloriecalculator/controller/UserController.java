package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.ProfileUpdateDTO;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/rest/user")
@EnableMethodSecurity(jsr250Enabled = true)
public class UserController {

    private IUserService userService;
    private UserModelAssembler modelAssembler;


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> findUserById(@PathVariable int id) {
        return new ResponseEntity<>(modelAssembler.toModel(userService.findUserById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUserData(@PathVariable int id, @RequestBody ProfileUpdateDTO profileUpdateDTO) {
        return new ResponseEntity<>(modelAssembler.toModel(userService.updateProfileOfUser(profileUpdateDTO, id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers() {
        return new ResponseEntity<>(modelAssembler.toCollectionModel(userService.findAll()), HttpStatus.OK);
    }

}
