package com.example.demo.controller;

import com.example.demo.entity.PgBooking;

import com.example.demo.entity.Owner;
import com.example.demo.entity.Pg;
import com.example.demo.service.PgBookingService;
import com.example.demo.service.OwnerService;
import com.example.demo.service.PgService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/owner/pg-bookings")
@CrossOrigin
public class BookingController {

    private final PgBookingService pgBookingService;
    private final OwnerService ownerService;
    private final PgService pgService;

    public BookingController(PgBookingService pgBookingService,
                             OwnerService ownerService,
                             PgService pgService) {
        this.pgBookingService = pgBookingService;
        this.ownerService = ownerService;
        this.pgService = pgService;
    }

    private Integer getLoggedOwnerId() {
        return (int) 1L;
    }

    @GetMapping
    public List<PgBooking> getPgBookings() {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());
        return pgBookingService.getOwnerBookings(owner);
    }

    @PostMapping
    public PgBooking createBooking(@RequestBody PgBooking pgBooking) {

        Integer pgId = pgBooking.getPg().getId();
        Pg pg = pgService.getById(pgId);

        pgBooking.setPg(pg);
        pgBooking.setStatus("PENDING");

        return pgBookingService.save(pgBooking);
    }

    @PutMapping("/{id}/confirm")
    public PgBooking confirm(@PathVariable Integer id) {
        return pgBookingService.confirmBooking(id);
    }
}
