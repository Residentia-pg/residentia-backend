package com.example.demo.controller;

import com.example.demo.entity.RegularUser;
import com.example.demo.repository.RegularUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private RegularUserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegularUser user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Error: Email already exists!");
            }
            user.setIsActive(true);
            return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegularUser loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .filter(u -> u.getPasswordHash().equals(loginRequest.getPasswordHash()))
                .map(u -> ResponseEntity.ok(u))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }
}