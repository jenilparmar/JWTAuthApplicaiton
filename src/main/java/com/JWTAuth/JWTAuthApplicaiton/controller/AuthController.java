package com.JWTAuth.JWTAuthApplicaiton.controller;

import com.JWTAuth.JWTAuthApplicaiton.dtos.LoginResponse;
import com.JWTAuth.JWTAuthApplicaiton.dtos.LoginUserDto;
import com.JWTAuth.JWTAuthApplicaiton.dtos.RegisterUserDto;
import com.JWTAuth.JWTAuthApplicaiton.entities.User;
import com.JWTAuth.JWTAuthApplicaiton.repositories.UserRepository;
import com.JWTAuth.JWTAuthApplicaiton.services.AuthenticationService;
import com.JWTAuth.JWTAuthApplicaiton.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService,
                          AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
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
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword creds) {

        String email = creds.getEmail();

        boolean emailExists = authenticationService.checkEmailExists(email);

        if (emailExists) {
            return ResponseEntity.ok("Email exists");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
