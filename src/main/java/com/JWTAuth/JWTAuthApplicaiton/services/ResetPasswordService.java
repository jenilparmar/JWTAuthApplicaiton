package com.JWTAuth.JWTAuthApplicaiton.services;

import com.JWTAuth.JWTAuthApplicaiton.entities.PasswordResetToken;
import com.JWTAuth.JWTAuthApplicaiton.entities.User;
import com.JWTAuth.JWTAuthApplicaiton.repositories.ResetPasswordRepository;
import com.JWTAuth.JWTAuthApplicaiton.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.Date;
@Service
@Transactional
public class ResetPasswordService {
    private UserRepository userRepository;
    private ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordService(
            UserRepository userRepository,
            ResetPasswordRepository resetPasswordRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.resetPasswordRepository = resetPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 bits
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String TokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

        String tokenHash = Base64.getEncoder().encodeToString(hash);
        return tokenHash;
    }


    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public PasswordResetToken addResetPasswordCred(String email , String tokenHash){

        PasswordResetToken token = new PasswordResetToken();

        Date expiryDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
        token.setEmail(email);
        token.setTokenHash(tokenHash);
        token.setUsed(false);
        token.setExpiresAt(expiryDate);
        resetPasswordRepository.save(token);
        return token;
    }
    public boolean resetPassword(String rawToken, String newPassword) {
        try {
            // 1. Hash incoming token
            String tokenHash = TokenHash(rawToken);

            // 2. Find token record
            PasswordResetToken token = resetPasswordRepository
                    .findByTokenHash(tokenHash)
                    .orElseThrow();

            // 3. Validate token
            if (token.isUsed()) {
                throw new RuntimeException("Token already used");
            }

            if (token.getExpiresAt().before(new Date())) {
                throw new RuntimeException("Token expired");
            }

            // 4. Find user by email
            User user = userRepository.findByEmail(token.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(newPassword));


            token.setUsed(true);

            userRepository.save(user);
            resetPasswordRepository.save(token);

            return true;

        } catch (Exception e) {
            System.out.println("Reset password failed ---> " + e.getMessage());
            return false;
        }
    }


}
