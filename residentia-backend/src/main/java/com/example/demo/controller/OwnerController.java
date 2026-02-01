package com.example.demo.controller;

import com.example.demo.entity.Owner;
import com.example.demo.entity.PgBooking;
import com.example.demo.repository.PgBookingRepository;
import com.example.demo.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    private PgBookingRepository pgBookingRepository;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Owner owner) {
        System.out.println("Received registration request for: " + owner.getEmail());
        try {
            Owner saved = ownerService.registerOwner(owner);
            System.out.println("Owner saved successfully with ID: " + saved.getId());
            return Map.of(
                    "ownerId", saved.getId(),
                    "email", saved.getEmail(),
                    "token", "mock-token-" + saved.getId() // Mock token for frontend
            );
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        System.out.println("Login attempt for email: " + credentials.get("email"));
        String email = credentials.get("email");
        String password = credentials.get("passwordHash") != null ? credentials.get("passwordHash")
                : credentials.get("password");

        try {
            Owner owner = ownerService.loginOwner(email, password);
            System.out.println("Login successful for owner: " + owner.getId());
            return Map.of(
                    "ownerId", owner.getId(),
                    "email", owner.getEmail(),
                    "token", "mock-token-" + owner.getId() // Mock token for frontend
            );
        } catch (Exception e) {
            System.err.println("Login failure: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/profile")
    public Owner getProfile(@RequestParam(required = false) Integer id) {
        // In real app, get from SecurityContext
        Integer ownerId = (id != null) ? id : 1;
        return ownerService.getOwner(ownerId);
    }

    @PutMapping("/profile")
    public Owner updateProfile(@RequestParam(required = false) Integer id, @RequestBody Owner owner) {
        Integer ownerId = (id != null) ? id : 1;
        return ownerService.updateOwner(ownerId, owner);
    }

    @GetMapping("/bookings")
    public List<PgBooking> getBookings(@RequestParam String email) {
        System.out.println("Fetching bookings for owner email: " + email);
        List<PgBooking> bookings = pgBookingRepository.findByPgIdOwnerEmail(email);
        System.out.println("Found " + bookings.size() + " bookings for " + email);
        return bookings;
    }

    @PutMapping("/bookings/{id}/approve")
    public PgBooking approveBooking(@PathVariable Integer id) {
        PgBooking booking = pgBookingRepository.findById(id).orElseThrow();
        booking.setStatus("APPROVED");
        return pgBookingRepository.save(booking);
    }

    @PutMapping("/bookings/{id}/reject")
    public PgBooking rejectBooking(@PathVariable Integer id) {
        PgBooking booking = pgBookingRepository.findById(id).orElseThrow();
        booking.setStatus("REJECTED");
        return pgBookingRepository.save(booking);
    }
}
