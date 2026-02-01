package com.residentia.controller;

import com.residentia.dto.BookingDTO;
import com.residentia.entity.Booking;
import com.residentia.entity.RegularUser;
import com.residentia.repository.RegularUserRepository;
import com.residentia.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/client")
@Tag(name = "Client Booking", description = "APIs for clients to create bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClientBookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @PostMapping("/properties/{propertyId}/bookings")
    @Operation(summary = "Create booking for a property")
    public ResponseEntity<?> createBooking(@PathVariable Long propertyId,
                                           @RequestBody BookingDTO bookingDTO,
                                           HttpServletRequest request) {
        try {
            String email = (String) request.getAttribute("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
            }

            RegularUser user = regularUserRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fill tenant info from logged-in user if not provided
            if (bookingDTO.getTenantName() == null) bookingDTO.setTenantName(user.getName());
            if (bookingDTO.getTenantEmail() == null) bookingDTO.setTenantEmail(user.getEmail());
            if (bookingDTO.getTenantPhone() == null) bookingDTO.setTenantPhone(user.getMobileNumber());

            Booking booking = bookingService.createBooking(propertyId, bookingDTO);

            BookingDTO response = bookingService.getBookingById(booking.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Failed to create booking: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/bookings")
    public ResponseEntity<?> getClientBookings(HttpServletRequest request) {
        try {
            String email = (String) request.getAttribute("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
            }
            List<BookingDTO> bookings = bookingService.getBookingsByClientEmail(email);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
