package com.JWTAuth.JWTAuthApplicaiton.entities;

import com.JWTAuth.JWTAuthApplicaiton.entities.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    //id
    //email
    //tokenHash
    //used
    //expiresat

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email ;
    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // hashed reset token

    // getters & setters
    public UUID getId() {
        return id;
    }


    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
