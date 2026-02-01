package com.example.demo.service;

import com.example.demo.entity.RegularUser;
import com.example.demo.repository.RegularUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegularUserService {

    private final RegularUserRepository userRepository;

    public RegularUserService(RegularUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegularUser registerUser(RegularUser user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (userRepository.findByMobileNumber(user.getMobileNumber()).isPresent()) {
            throw new RuntimeException("Mobile number already in use");
        }
        // In a real app, hash the password here
        return userRepository.save(user);
    }

    public RegularUser loginUser(String email, String password) {
        RegularUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    public RegularUser getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public RegularUser updateUser(Integer id, RegularUser updatedUser) {
        RegularUser existingUser = getUserById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setMobileNumber(updatedUser.getMobileNumber());
        // Add other fields as needed

        return userRepository.save(existingUser);
    }
}
