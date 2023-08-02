package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.User;
import jakarta.persistence.NamedEntityGraph;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
    User findByEmail(String email);

    @Query("select u from user_account u join fetch u.roles where u.email = :email")
    User findByEmailAndFetchRoles(@Param("email") String email);

    @Query("select u from user_account  u join fetch u.calorieIntakeList cil where cil.id = :id")
    User findUserByCalorieIntake(@Param("id") int id);

    @Query("select u from user_account u join fetch u.sportList sl where  sl.id = :id")
    User findUserBySport(@Param("id") int id);
}
