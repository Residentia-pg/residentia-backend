package com.example.demo.controller;

import com.example.demo.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/cleanup")
@CrossOrigin
public class DataCleanupController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PgRepository pgRepository;

    @PostMapping("/reset-demo-data")
    public Map<String, String> resetDemoData() {
        try {
            // Delete all incomplete PGs (those with null names)
            pgRepository.deleteAll(pgRepository.findAll().stream()
                    .filter(pg -> pg.getName() == null || pg.getName().isEmpty())
                    .toList());

            return Map.of(
                    "status", "success",
                    "message", "Incomplete PGs deleted. Please restart the backend to trigger fresh seeding.");
        } catch (Exception e) {
            return Map.of(
                    "status", "error",
                    "message", "Failed to clean up: " + e.getMessage());
        }
    }

    @PostMapping("/delete-all-pgs")
    @Transactional
    public Map<String, String> deleteAllPgs() {
        try {
            // Delete in correct order to avoid foreign key constraints
            entityManager.createNativeQuery("DELETE FROM wishlist").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM bookings").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM change_requests").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM pgs").executeUpdate();

            return Map.of(
                    "status", "success",
                    "message", "All PGs deleted successfully! Backend will auto-restart and re-seed with fresh data.");
        } catch (Exception e) {
            return Map.of(
                    "status", "error",
                    "message", "Failed to delete: " + e.getMessage());
        }

    }

    @PostMapping("/fix-duplicates")
    @Transactional
    public Map<String, String> fixDuplicates() {
        try {
            // Remove duplicate Owners (keep one with lowest ID)
            entityManager.createNativeQuery(
                    "DELETE t1 FROM pg_owners t1 " +
                            "INNER JOIN pg_owners t2 " +
                            "WHERE t1.id > t2.id AND t1.email = t2.email")
                    .executeUpdate();

            // Remove duplicate Regular Users
            entityManager.createNativeQuery(
                    "DELETE t1 FROM regular_users t1 " +
                            "INNER JOIN regular_users t2 " +
                            "WHERE t1.id > t2.id AND t1.email = t2.email")
                    .executeUpdate();

            return Map.of(
                    "status", "success",
                    "message", "Duplicate owners and clients removed successfully!");
        } catch (Exception e) {
            return Map.of(
                    "status", "error",
                    "message", "Failed to fix duplicates: " + e.getMessage());
        }
    }
}
