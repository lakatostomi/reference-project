package com.example.caloriecalculator.model;



import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
//@Table(schema = "caloriecalculator")
@Entity(name = "privilege")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }
}
