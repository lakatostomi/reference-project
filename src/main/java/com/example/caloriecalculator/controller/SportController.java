package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.SportDTO;
import com.example.caloriecalculator.model.Sport;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.model.assemblers.UserModelAssembler;
import com.example.caloriecalculator.service.interfaces.ISportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rest/user/sport")
@Tag(name = "SportController", description = "Handles endpoints that are connected with User's sport activity")
public class SportController {

    private UserModelAssembler modelAssembler;
    private ISportService sportService;

    @Operation(summary = "Saving new sport activity for User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Saving is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "Fields are not valid, RequestBody is missing"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json"))})
    @PostMapping
    public ResponseEntity<EntityModel<User>> saveSportActivity(@Parameter(description = "Contains values of sport activity") @RequestBody @Valid SportDTO sportDTO) {
        User user = sportService.saveUsersSport(sportDTO);
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }

    @Operation(summary = "Returning a sport activity by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Sport activity is exists",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Sport.class))),
            @ApiResponse(responseCode = "400", description = "Path-variable is missing"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json"))})
    @GetMapping("/{id}")
    public ResponseEntity<Sport> findSportById(@Parameter(description = "The ID of sport activity") @PathVariable Integer id) {
        return new ResponseEntity<>(sportService.findSportById(id), HttpStatus.OK);
    }

    @Operation(summary = "Updating sport activity for User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updating is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {User.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "RequestParams are missing"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json"))})
    @PutMapping()
    public ResponseEntity<EntityModel<User>> updateSportActivity(@Parameter(description = "The ID of sport") @RequestParam("id") Integer id,
                                                                 @Parameter(description = "The new value of the activity") @RequestParam("calories") Double calories) {
        User user = sportService.updateSport(id, calories);
        return new ResponseEntity<>(modelAssembler.toModel(user), HttpStatus.OK);
    }

    @Operation(summary = "Deleting sport activity")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Deleting is successful",
            content = @Content()),
            @ApiResponse(responseCode = "400", description = "Path-variable is missing, sport is not exists"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSportActivity(@Parameter(description = "The ID of sport activity") @PathVariable Integer id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}
