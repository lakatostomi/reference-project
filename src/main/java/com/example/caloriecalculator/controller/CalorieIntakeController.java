package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.IntakeDTO;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.service.interfaces.ICalorieIntakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/rest/user/intake")
@AllArgsConstructor
@Slf4j
@Tag(name = "CalorieIntakeController", description = "Handles request connected with User's calorie intake")
public class CalorieIntakeController {

    private ICalorieIntakeService intakeService;
    private UserModelAssembler modelAssembler;

    @Operation(summary = "Saving new calorie intake for User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Saving is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "The User is not exists, Path-variable is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @PostMapping
    public ResponseEntity<EntityModel<User>> saveNewCalorieIntake(@Parameter(description = "Contains the values of calorie intake") @RequestBody @Valid IntakeDTO intakeDTO) {
        User user = intakeService.saveUsersCalorieIntake(intakeDTO);
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }

    @Operation(summary = "Updating User's calorie intake")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updating is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "The Intake is not exists, RequestParams are missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @PutMapping
    public ResponseEntity<EntityModel<User>> updateCalorieIntake(@Parameter(description = "The ID of calorie intake") @RequestParam Integer id,
                                                                 @Parameter(description = "The new value of intake") @RequestParam Double quantity) {
        User user = intakeService.updateCalorieIntake(id, quantity);
        return ResponseEntity.ok(modelAssembler.toModel(user));
    }

    @Operation(summary = "Returning User's calorie intake by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Calorie intake is exists",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = CalorieIntake.class))),
            @ApiResponse(responseCode = "400", description = "The Intake is not exists, Path-variable is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public ResponseEntity<CalorieIntake> findIntakeById(@Parameter(description = "The ID of intake") @PathVariable Integer id) {
        return ResponseEntity.ok(intakeService.findIntakeById(id));
    }

    @Operation(summary = "Deleting User's calorie intake")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Deleting is successful",
            content = @Content()),
            @ApiResponse(responseCode = "400", description = "The Intake is not exists, Path-variable is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalorieIntake(@Parameter(description = "The ID of calorie intake") @PathVariable Integer id) {
        intakeService.deleteCalorieIntake(id);
        return ResponseEntity.noContent().build();
    }
}
