package com.JWTAuth.JWTAuthApplicaiton.controller;

import com.JWTAuth.JWTAuthApplicaiton.dtos.LoginResponse;
import com.JWTAuth.JWTAuthApplicaiton.dtos.LoginUserDto;
import com.JWTAuth.JWTAuthApplicaiton.dtos.RegisterUserDto;
import com.JWTAuth.JWTAuthApplicaiton.entities.User;
import com.JWTAuth.JWTAuthApplicaiton.services.AuthenticationService;
import com.JWTAuth.JWTAuthApplicaiton.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        authenticationService.signup(dto);
        return ResponseEntity.ok("User registered successfully");
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
}
