package com.residentia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.residentia.entity.Booking;
import com.residentia.service.BookingService;

@RestController
@RequestMapping("/api/admin/pg-bookings")
@CrossOrigin(origins="*")
public class AdminPgBookingController {

    private final BookingService service;

    public AdminPgBookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAll() { 
        // Assuming BookingService has a getAllBookings or similar method. 
        // Since original had getAllBookings(), keeping it.
        return ResponseEntity.ok(service.getAllBookings());
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) { 
        return ResponseEntity.ok(service.cancelBooking(id));
    }
    
    @PutMapping("/{id}/restore")
    public ResponseEntity<Booking> restore(@PathVariable Long id) {
        return ResponseEntity.ok(service.restoreBooking(id));
    }

}
