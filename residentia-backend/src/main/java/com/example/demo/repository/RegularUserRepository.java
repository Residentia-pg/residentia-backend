package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RegularUser;

@Repository
public interface RegularUserRepository extends JpaRepository<RegularUser, Integer> {

}
