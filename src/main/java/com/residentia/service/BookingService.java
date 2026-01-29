package com.residentia.service;

import com.residentia.dto.BookingDTO;
import com.residentia.entity.Booking;
import com.residentia.entity.Property;
import com.residentia.exception.ResourceNotFoundException;
import com.residentia.repository.BookingRepository;
import com.residentia.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public Booking createBooking(Long propertyId, BookingDTO bookingDTO) {
        log.info("Creating booking for property: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setStatus(bookingDTO.getStatus() != null ? bookingDTO.getStatus() : "PENDING");

        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long bookingId, BookingDTO bookingDTO) {
        log.info("Updating booking: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (bookingDTO.getStatus() != null) booking.setStatus(bookingDTO.getStatus());

        return bookingRepository.save(booking);
    }

    public BookingDTO getBookingById(Long bookingId) {
        log.info("Fetching booking: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        return convertToDTO(booking);
    }

    public List<BookingDTO> getOwnerBookings(Long ownerId) {
        log.info("Fetching bookings for owner: {}", ownerId);

        List<Booking> bookings = bookingRepository.findByPropertyOwnerId(ownerId);
        return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<BookingDTO> getPropertyBookings(Long propertyId) {
        log.info("Fetching bookings for property: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        List<Booking> bookings = bookingRepository.findByProperty(property);
        return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deleteBooking(Long bookingId) {
        log.info("Deleting booking: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        bookingRepository.delete(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking cancelBooking(Long bookingId) {
        log.info("Cancelling booking: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }

    public Booking restoreBooking(Long bookingId) {
        log.info("Restoring booking: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getId());
        dto.setPropertyId(booking.getProperty().getId());
        dto.setPropertyName(booking.getProperty().getPropertyName());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
