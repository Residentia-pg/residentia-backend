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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="pgs")
public class Pg 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(name="property_name")
    private String propertyName;
    
    @Column(name="address")
    private String address;
    
    private String city;
    private String state;
    
    @Column(name="rent_amount")
    private Integer rentAmount;
    
    @Column(name="sharing_type")
    private String sharingType;
    
    @Column(name="max_capacity")
    private Integer maxCapacity;

    @Column(name="available_beds")
    private Integer availableBeds;

    @Column(name="food_included")
    private Boolean foodIncluded=false;
    
    private String pincode;

    private String status; // PENDING, ACTIVE, REJECTED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
