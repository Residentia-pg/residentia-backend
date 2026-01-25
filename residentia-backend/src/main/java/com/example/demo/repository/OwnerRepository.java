package com.example.demo.repository;

import com.example.demo.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Optional<Owner> findByEmail(String email);
    long countByVerificationStatus(String status);
}
