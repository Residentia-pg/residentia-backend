package com.example.demo.repository;

import com.example.demo.entity.Pg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PgRepository extends JpaRepository<Pg, Integer> {
    List<Pg> findByLocationContainingIgnoreCase(String location);

    List<Pg> findByOwnerEmail(String ownerEmail);

    // Find only ACTIVE PGs for client-facing API
    List<Pg> findByStatus(String status);

    List<Pg> findByStatusAndLocationContainingIgnoreCase(String status, String location);
}
