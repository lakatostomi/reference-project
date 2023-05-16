package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.ProfileUpdateDTO;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "UserController", description = "Handles endpoint that are connected with User")
public class UserController {

    private IUserService userService;
    private UserModelAssembler modelAssembler;

    @Operation(summary = "Returning a User by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The User has found",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "The User is not exist, Path-variable is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> findUserById(@Parameter(description = "The ID of the User") @PathVariable int id) {
        return new ResponseEntity<>(modelAssembler.toModel(userService.findUserById(id)), HttpStatus.OK);
    }

    @Operation(summary = "Updating User's profile")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Update is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "The User is not exists, Path-variable or request body is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUserData(@Parameter(description = "The ID of User") @PathVariable int id,
                                                            @Parameter(description = "Contains the values of User") @RequestBody ProfileUpdateDTO profileUpdateDTO) {
        return new ResponseEntity<>(modelAssembler.toModel(userService.updateProfileOfUser(profileUpdateDTO, id)), HttpStatus.OK);
    }

    @Operation(summary = "Deleting existing User from DB")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Delete is successful",
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "The User not exists, Path-variable is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "The ID of User") @PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "List all User from DB")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Collection model successfully created",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, CollectionModel.class}))),
            @ApiResponse(responseCode = "403", description = "Sending request to the endpoint with USER_ROLE"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers() {
        return new ResponseEntity<>(modelAssembler.toCollectionModel(userService.findAll()), HttpStatus.OK);
    }
}
