package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RegularUser;
import java.util.Optional;

@Repository
public interface RegularUserRepository extends JpaRepository<RegularUser, Integer> {

    Optional<RegularUser> findByEmail(String email);

    Optional<RegularUser> findByMobileNumber(String mobileNumber);
}
