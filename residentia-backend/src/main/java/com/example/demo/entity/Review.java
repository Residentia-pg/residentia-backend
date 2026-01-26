package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pg_id", nullable = false)
    private Pg pg;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private RegularUser user;

    private Integer rating;
    
    @Column(name="review_text")
    private String reviewText;
    
    @Column(name="is_anonymous")
    private Boolean isAnonymous = false;

    private String status; // PENDING, APPROVED

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
