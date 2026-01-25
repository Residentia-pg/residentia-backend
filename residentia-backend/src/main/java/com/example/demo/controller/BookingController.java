package com.example.demo.controller;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Owner;
import com.example.demo.entity.Property;
import com.example.demo.service.BookingService;
import com.example.demo.service.OwnerService;
import com.example.demo.service.PropertyService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/owner/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;
    private final OwnerService ownerService;
    private final PropertyService propertyService;

    public BookingController(BookingService bookingService,
                             OwnerService ownerService,
                             PropertyService propertyService) {
        this.bookingService = bookingService;
        this.ownerService = ownerService;
        this.propertyService = propertyService;
    }

    private Long getLoggedOwnerId() {
        return 1L;
    }

    @GetMapping
    public List<Booking> getBookings() {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());
        return bookingService.getOwnerBookings(owner);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());

        Long propertyId = booking.getProperty().getId();
        Property property = propertyService.getById(propertyId);

        booking.setProperty(property);
        booking.setOwner(owner);
        booking.setStatus("PENDING");

        return bookingService.save(booking);
    }

    @PutMapping("/{id}/confirm")
    public Booking confirm(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }
}
