package com.JWTAuth.JWTAuthApplicaiton.controller;

import com.JWTAuth.JWTAuthApplicaiton.dtos.LoginResponse;
import com.JWTAuth.JWTAuthApplicaiton.dtos.LoginUserDto;
import com.JWTAuth.JWTAuthApplicaiton.dtos.RegisterUserDto;
import com.JWTAuth.JWTAuthApplicaiton.entities.PasswordResetToken;
import com.JWTAuth.JWTAuthApplicaiton.entities.User;
import com.JWTAuth.JWTAuthApplicaiton.repositories.UserRepository;
import com.JWTAuth.JWTAuthApplicaiton.services.AuthenticationService;
import com.JWTAuth.JWTAuthApplicaiton.services.JwtService;
import com.JWTAuth.JWTAuthApplicaiton.services.MailService;
import com.JWTAuth.JWTAuthApplicaiton.services.ResetPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final ResetPasswordService resetPasswordService;
    private final MailService mailService;
    public AuthController(JwtService jwtService,
                          AuthenticationService authenticationService, ResetPasswordService resetPasswordService , MailService mailService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.resetPasswordService = resetPasswordService;
        this.mailService = mailService;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto dto) {
        try {
            System.out.println("========================================");
            System.out.println("SIGNUP CONTROLLER HIT!");
            System.out.println("Email: " + dto.getEmail());
            System.out.println("Full Name: " + dto.getFullName());
            System.out.println("========================================");

            User user = authenticationService.signup(dto);

            // Return JSON instead of plain string
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("SIGNUP ERROR: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(error);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginUserDto dto) {

        User user = authenticationService.authenticate(dto);
        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse()
                .setToken(token)
                .setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ResetPassword creds) throws NoSuchAlgorithmException {

        String email = creds.getEmail();

        boolean emailExists = resetPasswordService.checkEmailExists(email);

        if (emailExists) {
            String tokenTosend = resetPasswordService.generateToken();
            String tokenHash = resetPasswordService.TokenHash(tokenTosend);
            PasswordResetToken token = resetPasswordService.addResetPasswordCred(email , tokenHash);
            boolean mailSended = mailService.sendMail(email , tokenTosend);
            System.out.println(mailSended?"😃😃mail sended":"😡😡mail not sended");
            return ResponseEntity.ok("Sended links");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reset-password")
    public boolean resetPassword(@RequestBody ResetPasswordRequest req) {
        return resetPasswordService.resetPassword(
                req.getToken(),
                req.getNewPassword()
        );
    }

}
