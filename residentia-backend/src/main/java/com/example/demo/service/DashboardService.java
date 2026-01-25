package com.example.demo.service;

import com.example.demo.entity.Owner;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;

    public DashboardService(PropertyRepository propertyRepository,
                            BookingRepository bookingRepository) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
    }

    public long getTotalProperties(Owner owner) {
        return propertyRepository.findByOwner(owner).size();
    }

    public long getTotalBookings(Owner owner) {
        return bookingRepository.findByOwner(owner).size();
    }
}
