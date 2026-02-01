package com.example.demo.service;

import com.example.demo.entity.Owner;
import com.example.demo.repository.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner getOwner(Integer id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
    }

    public Owner updateOwner(Integer id, Owner updated) {
        Owner owner = getOwner(id);
        owner.setName(updated.getName());
        owner.setEmail(updated.getEmail());
        owner.setPhone(updated.getPhone());
        owner.setCity(updated.getCity());
        return ownerRepository.save(owner);
    }

    public Owner registerOwner(Owner owner) {
        if (ownerRepository.findByEmail(owner.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        // In real app, hash password here
        return ownerRepository.save(owner);
    }

    public Owner loginOwner(String email, String password) {
        System.out.println("Service: Attempting login for email: " + email);
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Owner not found with email: " + email));

        System.out.println("Service: Found owner in DB. Comparing passwords...");
        System.out.println("Service: Password in DB: [" + owner.getPasswordHash() + "]");
        System.out.println("Service: Password provided: [" + password + "]");

        // In real app use BCrypt
        if (owner.getPasswordHash() == null || !owner.getPasswordHash().equals(password)) {
            System.err.println("Service: Password mismatch or missing!");
            throw new RuntimeException("Invalid credentials");
        }
        System.out.println("Service: Login successful for: " + email);
        return owner;
    }
}
