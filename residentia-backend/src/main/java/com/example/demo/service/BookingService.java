//package com.example.demo.service;
//
//import com.example.demo.entity.Booking;
//import com.example.demo.entity.Owner;
//import com.example.demo.repository.BookingRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class BookingService {
//
//    private final BookingRepository bookingRepository;
//
//    public BookingService(BookingRepository bookingRepository) {
//        this.bookingRepository = bookingRepository;
//    }
//    
//    public Booking save(Booking booking) {
//        return bookingRepository.save(booking);
//    }
//
//
//    public List<Booking> getOwnerBookings(Owner owner) {
//        return bookingRepository.findByOwner(owner);
//    }
//
//    public Booking confirmBooking(Long id) {
//        Booking booking = bookingRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//        booking.setStatus("CONFIRMED");
//        return bookingRepository.save(booking);
//    }
//}
