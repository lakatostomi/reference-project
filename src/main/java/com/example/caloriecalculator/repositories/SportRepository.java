package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {
}
