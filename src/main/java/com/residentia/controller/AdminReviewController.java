package com.residentia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.residentia.entity.Review;
import com.residentia.repository.ReviewRepository;

@RestController
@RequestMapping("/api/admin/reviews")
@CrossOrigin(origins="*")
public class AdminReviewController {

    private final ReviewRepository reviewRepo;

    public AdminReviewController(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    //getting all reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAll() { 
        return ResponseEntity.ok(reviewRepo.findAll());
    }

    //Approving review
    @PutMapping("/{id}/approve")
    public ResponseEntity<Review> approve(@PathVariable Integer id) { 
        Review r = reviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        r.setStatus("APPROVED");
        return ResponseEntity.ok(reviewRepo.save(r));
    }
    
    //reject review
    @PutMapping("/{id}/reject")
    public ResponseEntity<Review> reject(@PathVariable Integer id) {
        Review review = reviewRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setStatus("REJECTED");
        return ResponseEntity.ok(reviewRepo.save(review));
    }
    
    //Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reviewRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

