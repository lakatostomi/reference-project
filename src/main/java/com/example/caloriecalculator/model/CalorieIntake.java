package com.example.caloriecalculator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
//@Table(schema = "caloriecalculator")
@Entity(name = "calorie_intake")
@AllArgsConstructor
public class CalorieIntake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date timeOfIntake;
    private Double quantityOfFood;
    @ManyToOne(targetEntity = Food.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id", foreignKey = @ForeignKey(name = "CI_FK_FOOD"))
    private Food food;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "CI_FK_USER"))
    private User user;
}
