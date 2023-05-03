package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);
    @Query("SELECT u FROM user_account u join fetch u.calorieIntakeList cl where u.email = :email and cl.timeOfIntake between :start and :end")
    User findByEmailAndFetchIntakes(@Param("email") String email, @Param("start") Date start, @Param("end") Date end);
}
