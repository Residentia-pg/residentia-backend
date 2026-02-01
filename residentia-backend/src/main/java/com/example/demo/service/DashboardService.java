package com.example.demo.service;

import com.example.demo.entity.Owner;

import com.example.demo.repository.PgBookingRepository;
import com.example.demo.repository.PgRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final PgRepository pgRepository;
    private final PgBookingRepository pgBookingRepository;

    public DashboardService(PgRepository pgRepository,
            PgBookingRepository pgBookingRepository) {
        this.pgRepository = pgRepository;
        this.pgBookingRepository = pgBookingRepository;
    }

    public long getTotalProperties(Owner owner) {
        return pgRepository.findByOwnerEmail(owner.getEmail()).size();
    }

    public long getTotalBookings(Owner owner) {
        return pgBookingRepository.findByPgIdOwnerEmail(owner.getEmail()).size();
    }
}
