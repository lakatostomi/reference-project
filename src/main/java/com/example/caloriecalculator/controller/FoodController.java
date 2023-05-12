package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.FoodDTO;
import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.model.assemblers.FoodModelAssembler;
import com.example.caloriecalculator.service.interfaces.IFoodService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@AllArgsConstructor
@RestController
@RequestMapping("/api/rest/foods")
@EnableMethodSecurity
public class FoodController {

    private IFoodService foodService;

    private FoodModelAssembler modelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Food>>> findAll() {
        CollectionModel<EntityModel<Food>> collectionModel = modelAssembler.toCollectionModel(foodService.findAll());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Food>> findById(@PathVariable int id) {
        EntityModel<Food> food = modelAssembler.toModel(foodService.findFoodById(id));
        return new ResponseEntity<>(food, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping
    public ResponseEntity<EntityModel<Food>> saveFood(@Valid @RequestBody FoodDTO foodDTO) {
        EntityModel<Food> food = modelAssembler.toModel(foodService.saveFood(foodDTO));
        return ResponseEntity.created(food.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(food);
    }
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Food>> updateFood(@Valid @RequestBody FoodDTO foodDTO, @PathVariable Integer id) {
        EntityModel<Food> food = modelAssembler.toModel(foodService.updateFood(foodDTO, id));
        return ResponseEntity.created(food.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(food);
    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Integer id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}
