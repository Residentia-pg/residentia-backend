package com.example.demo.service;

import com.example.demo.entity.Owner;

import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.PgRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final PgRepository pgRepository;
    private final BookingRepository bookingRepository;

    public DashboardService(PgRepository pgRepository,
                            BookingRepository bookingRepository) {
        this.pgRepository = pgRepository;
        this.bookingRepository = bookingRepository;
    }

    public long getTotalProperties(Owner owner) {
        return pgRepository.findByOwner(owner).size();
    }

    public long getTotalBookings(Owner owner) {
        return bookingRepository.findByOwner(owner).size();
    }
}
