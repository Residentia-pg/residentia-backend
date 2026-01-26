package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PgBooking;
import com.example.demo.entity.Owner;
import com.example.demo.entity.Pg;

public interface PgRepository extends JpaRepository<Pg,Integer>
{
	long countByStatus(String status);

	List<Pg> findByOwner(Owner owner);
}
