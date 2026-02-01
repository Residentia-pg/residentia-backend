package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;

@RestController
@RequestMapping("/api/admin/reviews")
@CrossOrigin
public class AdminReviewController {

    private final ReviewRepository reviewRepo;

    public AdminReviewController(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    // getting all reviews
    @GetMapping
    public List<Review> getAll() {
        return reviewRepo.findAll();
    }

    // Approving review
    @PutMapping("/{id}/approve")
    public Review approve(@PathVariable Integer id) {
        Review r = reviewRepo.findById(id).orElseThrow();
        r.setStatus("APPROVED");
        return reviewRepo.save(r);
    }

    // Delete review
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        reviewRepo.deleteById(id);
    }
}
