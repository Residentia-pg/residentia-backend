package com.example.demo.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminAuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> creds) {
        String email = creds.get("email");
        String password = creds.get("password");

        if ("admin@residentia.com".equals(email) && "Admin@123".equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "id", 1, // Mock ID for frontend
                    "adminId", 1,
                    "email", email,
                    "role", "ADMIN",
                    "name", "System Admin"));
        }

        return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Invalid admin credentials"));
    }
}
