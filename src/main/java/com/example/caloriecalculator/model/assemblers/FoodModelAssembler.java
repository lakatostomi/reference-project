package com.example.caloriecalculator.model.assemblers;

import com.example.caloriecalculator.controller.FoodController;
import com.example.caloriecalculator.model.Food;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FoodModelAssembler implements RepresentationModelAssembler<Food, EntityModel<Food>> {

    @Override
    public EntityModel<Food> toModel(Food entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(FoodController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(FoodController.class).findAll()).withRel("foods"));
    }

    @Override
    public CollectionModel<EntityModel<Food>> toCollectionModel(Iterable<? extends Food> entities) {
        List<EntityModel<Food>> foods = new ArrayList<>();
        Iterator<Food> itr = (Iterator<Food>) entities.iterator();
        while (itr.hasNext()) {
            EntityModel<Food> food = toModel(itr.next());
            foods.add(food);
        }
        return CollectionModel.of(foods, linkTo(methodOn(FoodController.class).findAll()).withSelfRel());
    }
}
