package com.example.demo.repository;

import com.example.demo.entity.ClientBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientBookingRepository extends JpaRepository<ClientBooking, Long> {
    List<ClientBooking> findByClientEmail(String clientEmail);

    List<ClientBooking> findByPgId(Long pgId);
}
