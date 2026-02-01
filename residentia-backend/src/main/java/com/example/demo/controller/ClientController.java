package com.example.demo.controller;

import com.example.demo.entity.RegularUser;
import com.example.demo.service.RegularUserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin // Allow all origins for dev
public class ClientController {

    private final RegularUserService userService;

    public ClientController(RegularUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegularUser register(@RequestBody RegularUser user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public RegularUser login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        return userService.loginUser(email, password);
    }

    @GetMapping("/{id}")
    public RegularUser getProfile(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public RegularUser updateProfile(@PathVariable Integer id, @RequestBody RegularUser user) {
        return userService.updateUser(id, user);
    }
}
