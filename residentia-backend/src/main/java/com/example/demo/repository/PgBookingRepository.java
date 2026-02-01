package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Owner;
import com.example.demo.entity.PgBooking;

import com.example.demo.entity.RegularUser;

public interface PgBookingRepository extends JpaRepository<PgBooking, Integer> {
	List<PgBooking> findByPgIdOwnerEmail(String ownerEmail);

	List<PgBooking> findByUser(RegularUser user);
}
