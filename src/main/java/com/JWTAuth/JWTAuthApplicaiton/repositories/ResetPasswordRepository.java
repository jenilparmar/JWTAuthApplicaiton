package com.JWTAuth.JWTAuthApplicaiton.repositories;

import com.JWTAuth.JWTAuthApplicaiton.entities.PasswordResetToken;

import com.JWTAuth.JWTAuthApplicaiton.entities.User;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;
import java.util.UUID;

public interface ResetPasswordRepository extends CrudRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);
}
