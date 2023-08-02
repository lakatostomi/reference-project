package com.example.caloriecalculator.repositories;

import com.example.caloriecalculator.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    VerificationToken findByToken(String token);
    @Transactional
    @Modifying
    @Query("DELETE FROM verification_token vt where vt.expiryDate < :limit")
    void deleteIfExpired(@Param("limit") Timestamp limit);
}
