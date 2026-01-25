package com.example.demo.repository;

import com.example.demo.entity.Property;
import com.example.demo.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByOwner(Owner owner);
}
