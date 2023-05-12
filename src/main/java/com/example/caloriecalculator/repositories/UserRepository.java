package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("select u from user_account u join fetch u.roles where u.email = :email")
    User findByEmailAndFetchRoles(@Param("email") String email);

}
