package com.example.demo.controller;

import com.example.demo.entity.Booking;

import com.example.demo.entity.Owner;
import com.example.demo.entity.Pg;
import com.example.demo.service.BookingService;
import com.example.demo.service.OwnerService;
import com.example.demo.service.PgService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/owner/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;
    private final OwnerService ownerService;
    private final PgService pgService;

    public BookingController(BookingService bookingService,
                             OwnerService ownerService,
                             PgService pgService) {
        this.bookingService = bookingService;
        this.ownerService = ownerService;
        this.pgService = pgService;
    }

    private Integer getLoggedOwnerId() {
        return (int) 1L;
    }

    @GetMapping
    public List<Booking> getBookings() {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());
        return bookingService.getOwnerBookings(owner);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());

        Integer pgId = booking.getPg().getId();
        Pg pg = pgService.getById(pgId);

        booking.setPg(pg);
        booking.setOwner(owner);
        booking.setStatus("PENDING");

        return bookingService.save(booking);
    }

    @PutMapping("/{id}/confirm")
    public Booking confirm(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }
}
