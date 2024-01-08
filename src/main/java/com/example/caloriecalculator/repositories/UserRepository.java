package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    /*Without EntityGraph Hibernate will generate 3 SELECT statement, with EntityGraph annotation I fetch one association(Calorie Intake)
    * and Hibernate will generate one more SELECT statement to fetch the second association (Sport),
    * all in all instead of 3 statements only 2 statements are executed */
    @EntityGraph(attributePaths = "calorieIntakeList")
    User findByEmail(String email);

    /*The concept is the same as above...I fetch the Role association and I use FetchType.EAGER strategy
     when Role entity fetches the Privilege association, so Hibernate will execute only one more SELECT statement which refers to
     join table of roles_privileges */
    @Query("select u from user_account u join fetch u.roles where u.email = :email")
    User findByEmailAndFetchRoles(@Param("email") String email);

    @Query("select u from user_account  u join fetch u.calorieIntakeList cil where cil.id = :id")
    User findUserByCalorieIntake(@Param("id") int id);

    @Query("select u from user_account u join fetch u.sportList sl where  sl.id = :id")
    User findUserBySport(@Param("id") int id);
}
