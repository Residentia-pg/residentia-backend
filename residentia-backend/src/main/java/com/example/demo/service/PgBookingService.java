package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Owner;
import com.example.demo.entity.PgBooking;
import com.example.demo.repository.PgBookingRepository;

@Service
public class PgBookingService 
{
	private final PgBookingRepository pgBookingRepository;

    public PgBookingService(PgBookingRepository pgBookingRepository) {
        this.pgBookingRepository = pgBookingRepository;
    }
    
    public List<PgBooking> getAllBookings() {
        return pgBookingRepository.findAll();
    }
    
    public PgBooking save(PgBooking pgBooking) {
        return pgBookingRepository.save(pgBooking);
    }


    public List<PgBooking> getOwnerBookings(Owner owner) {
        return pgBookingRepository.findByPgOwner(owner);
    }

    public PgBooking cancelBooking(Integer id) {
        PgBooking pgBooking = pgBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        pgBooking.setStatus("CANCELLED");
        return pgBookingRepository.save(pgBooking);
    }
    
    public PgBooking confirmBooking(Integer id) {

        PgBooking pgBooking = pgBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"PENDING".equals(pgBooking.getStatus())) {
            throw new RuntimeException("Only pending bookings can be confirmed");
        }

        pgBooking.setStatus("CONFIRMED");
        return pgBookingRepository.save(pgBooking);
    }
}
