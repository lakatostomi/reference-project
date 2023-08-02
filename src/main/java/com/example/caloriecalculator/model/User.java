package com.example.caloriecalculator.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
//@Table(schema = "caloriecalculator")
@Entity(name = "user_account")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Double height;
    private Double weight;

//  1=lightly active /mostly sitting - office worker/
//  2=moderately active/mostly standing - teacher, cashier/
//  3=active /mostly walking - sales/
//  4=very active / physically demanding job/
    private Integer activity;
    @JsonIgnore
    @Column(length = 60, nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CalorieIntake> calorieIntakeList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Sport> sportList;

    public User(String name, String password, String email, boolean enabled) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.sportList = new ArrayList<>();
        this.calorieIntakeList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
