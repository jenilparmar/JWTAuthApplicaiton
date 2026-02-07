package com.JWTAuth.JWTAuthApplicaiton.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    @PostMapping("/me")
    public ResponseEntity<String> handleUsersMe(){
        return ResponseEntity.ok("Hey Mee!!");
    }

    @GetMapping
    public  ResponseEntity<String> handleUsers(){
        return ResponseEntity.ok("Hey this is all users");
    }
}
