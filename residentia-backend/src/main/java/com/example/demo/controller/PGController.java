package com.example.demo.controller;

import com.example.demo.entity.Pg;
import com.example.demo.entity.PgBooking;
import com.example.demo.entity.RegularUser;
import com.example.demo.repository.PgRepository;
import com.example.demo.repository.PgBookingRepository;
import com.example.demo.repository.RegularUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pgs")
@CrossOrigin
public class PGController {

    @Autowired
    private PgRepository pgRepository;

    @Autowired
    private PgBookingRepository pgBookingRepository;

    @Autowired
    private RegularUserRepository userRepository;

    // Get all ACTIVE PGs (with optional search) - clients should only see ACTIVE
    // PGs
    @GetMapping
    public List<Pg> getAllPGs(@RequestParam(required = false) String location) {
        if (location != null && !location.isEmpty()) {
            return pgRepository.findByStatusAndLocationContainingIgnoreCase("ACTIVE", location);
        }
        return pgRepository.findByStatus("ACTIVE");
    }

    // Add a new PG (Owner)
    @PostMapping
    public Pg addPG(@RequestBody Pg pg) {
        // Construct location if city/state are provided
        if ((pg.getLocation() == null || pg.getLocation().isEmpty()) && pg.getCity() != null) {
            String city = pg.getCity();
            String state = pg.getState();
            pg.setLocation(state != null ? city + ", " + state : city);
        }

        if (pg.getStatus() == null || pg.getStatus().isEmpty()) {
            pg.setStatus("ACTIVE");
        }

        return pgRepository.save(pg);
    }

    // Book a PG
    @PostMapping("/{pgId}/book")
    public PgBooking bookPG(@PathVariable Integer pgId, @RequestBody Map<String, String> bookingRequest) {
        Pg pg = pgRepository.findById(pgId).orElseThrow(() -> new RuntimeException("PG not found"));

        // Get or create user (mock logic for prototype)
        String email = bookingRequest.get("clientEmail");
        RegularUser user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new RegularUser();
            user.setEmail(email);
            user.setName(bookingRequest.get("clientName"));
            user.setMobileNumber(
                    bookingRequest.get("clientPhone") != null ? bookingRequest.get("clientPhone") : "0000000000");
            user.setPasswordHash("password"); // default
            user = userRepository.save(user);
        }

        PgBooking booking = new PgBooking();
        booking.setPgId(pg); // The entity expects Pg object
        booking.setUser(user);
        booking.setStartDate(java.time.LocalDate.now());
        booking.setEndDate(java.time.LocalDate.now().plusMonths(1));

        // All bookings start as PENDING and require owner approval
        booking.setStatus("PENDING");

        return pgBookingRepository.save(booking);
    }

    @GetMapping("/user/{userId}/bookings")
    public List<PgBooking> getBookingsByUser(@PathVariable Integer userId) {
        RegularUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return pgBookingRepository.findByUser(user);
    }

    // Get single PG by ID
    @GetMapping("/{id}")
    public Pg getPGById(@PathVariable Integer id) {
        return pgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + id));
    }

    // Update PG
    @PutMapping("/{id}")
    public Pg updatePG(@PathVariable Integer id, @RequestBody Pg updatedPg) {
        Pg existingPg = pgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + id));

        // Update fields
        if (updatedPg.getName() != null)
            existingPg.setName(updatedPg.getName());
        if (updatedPg.getCity() != null)
            existingPg.setCity(updatedPg.getCity());
        if (updatedPg.getState() != null)
            existingPg.setState(updatedPg.getState());
        if (updatedPg.getPincode() != null)
            existingPg.setPincode(updatedPg.getPincode());
        if (updatedPg.getLocation() != null)
            existingPg.setLocation(updatedPg.getLocation());
        if (updatedPg.getPrice() != null)
            existingPg.setPrice(updatedPg.getPrice());
        if (updatedPg.getAvailableBeds() != null)
            existingPg.setAvailableBeds(updatedPg.getAvailableBeds());
        if (updatedPg.getAmenities() != null)
            existingPg.setAmenities(updatedPg.getAmenities());
        if (updatedPg.getImageUrl() != null)
            existingPg.setImageUrl(updatedPg.getImageUrl());
        if (updatedPg.getStatus() != null)
            existingPg.setStatus(updatedPg.getStatus());
        if (updatedPg.getDescription() != null)
            existingPg.setDescription(updatedPg.getDescription());

        return pgRepository.save(existingPg);
    }

    // Delete PG
    @DeleteMapping("/{id}")
    public Map<String, String> deletePG(@PathVariable Integer id) {
        Pg pg = pgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + id));
        pgRepository.delete(pg);
        return Map.of("message", "PG deleted successfully", "id", id.toString());
    }

    // Get PGs by owner email
    @GetMapping("/owner/{email}")
    public List<Pg> getPGsByOwnerEmail(@PathVariable String email) {
        return pgRepository.findByOwnerEmail(email);
    }
}
