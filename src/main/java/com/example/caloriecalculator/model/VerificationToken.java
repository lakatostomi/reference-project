package com.example.caloriecalculator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
//@Table(schema = "caloriecalculator")
@Entity(name = "verification_token")
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "VERIFY_USER"))
    private User user;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = new Date(System.currentTimeMillis() + 86_400_000L); //1 day
    }

    public VerificationToken(String token, long expiryDate, User user) {
        this.token = token;
        this.expiryDate = new Date(expiryDate);
        this.user = user;
    }


}
