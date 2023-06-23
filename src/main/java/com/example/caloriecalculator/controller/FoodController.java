package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.FoodDTO;
import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.model.assemblers.FoodModelAssembler;
import com.example.caloriecalculator.service.interfaces.IFoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rest/foods")
@EnableMethodSecurity
@Tag(name = "FoodController", description = "Handles endpoints that are connected with Food")
public class FoodController {

    private IFoodService foodService;

    private FoodModelAssembler modelAssembler;

    @Operation(summary = "Returning a collection-model of all foods")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Creating collection-model was successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {Food.class, CollectionModel.class}))),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json"))})
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Food>>> findAll() {
        CollectionModel<EntityModel<Food>> collectionModel = modelAssembler.toCollectionModel(foodService.findAll());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @Operation(summary = "Returning a Food by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Food is exists",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {Food.class, EntityModel.class}))),
            @ApiResponse(responseCode = "400", description = "Path-variable is missing"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json"))})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Food>> findById(@Parameter(description = "The ID of Food") @PathVariable int id) {
        EntityModel<Food> food = modelAssembler.toModel(foodService.findFoodById(id));
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @Operation(summary = "Saving new Food")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Saving is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {Food.class, EntityModel.class}))),
            @ApiResponse(responseCode = "403", description = "Sending request to the endpoint with READ_PRIVILEGE"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "400", description = "Fields are not valid, RequestBody is missing",
                    content = @Content(mediaType = "application/problem+json"))})
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping
    public ResponseEntity<EntityModel<Food>> saveFood(@Parameter(description = "Contains values of Food") @Valid @RequestBody FoodDTO foodDTO) {
        EntityModel<Food> food = modelAssembler.toModel(foodService.saveFood(foodDTO));
        return ResponseEntity.created(food.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(food);
    }

    @Operation(summary = "Updating existing Food")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updating is successful",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(allOf = {Food.class, EntityModel.class}))),
            @ApiResponse(responseCode = "403", description = "Sending request to the endpoint with READ_PRIVILEGE"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "400", description = "Fields are not valid, RequestBody or Path-variable is missing",
                    content = @Content(mediaType = "application/problem+json"))})
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Food>> updateFood(@Parameter(description = "Contains values of Food") @Valid @RequestBody FoodDTO foodDTO,
                                                        @Parameter(description = "The ID of Food") @PathVariable Integer id) {
        EntityModel<Food> food = modelAssembler.toModel(foodService.updateFood(foodDTO, id));
        return ResponseEntity.created(food.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(food);
    }

    @Operation(summary = "Deleting existing Food")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Deleting is successful",
            content = @Content()),
            @ApiResponse(responseCode = "403", description = "Sending request to the endpoint with READ_PRIVILEGE"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "400", description = "Path-variable is missing",
                    content = @Content(mediaType = "application/problem+json"))})
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@Parameter(description = "The ID of Food") @PathVariable Integer id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}
