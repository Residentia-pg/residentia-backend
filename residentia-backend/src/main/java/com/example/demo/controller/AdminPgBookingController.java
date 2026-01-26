package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PgBooking;
import com.example.demo.service.PgBookingService;

@RestController
@RequestMapping("/api/admin/pg-bookings")
public class AdminPgBookingController {

    private final PgBookingService service;

    public AdminPgBookingController(PgBookingService service) {
        this.service = service;
    }

    @GetMapping
    public List<PgBooking> getAll() {
        return service.getAllBookings();
    }

    @PutMapping("/{id}/cancel")
    public PgBooking cancel(@PathVariable Integer id) {
        return service.cancelBooking(id);
    }
}
