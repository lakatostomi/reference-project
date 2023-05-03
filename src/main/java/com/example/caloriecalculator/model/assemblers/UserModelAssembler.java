package com.example.caloriecalculator.model.assemblers;

import com.example.caloriecalculator.controller.CalorieIntakeController;
import com.example.caloriecalculator.controller.UserController;
import com.example.caloriecalculator.model.CalorieIntake;
import com.example.caloriecalculator.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
         EntityModel<User> entityModel = EntityModel.of(entity, linkTo(methodOn(UserController.class).findUserById(entity.getId())).withSelfRel());
         if (!entityModel.getContent().getCalorieIntakeList().isEmpty()) {
             for (CalorieIntake calorieIntake : entityModel.getContent().getCalorieIntakeList())
                 entityModel.add(linkTo(methodOn(CalorieIntakeController.class).findIntakeById(calorieIntake.getId())).withRel("allIntake"));
         }
         return entityModel;
    }
}
