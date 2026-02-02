package com.residentia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@com.fasterxml.jackson.databind.annotation.JsonSerialize(as = Booking.class)
@NoArgsConstructor   // ✅ REQUIRED by Hibernate
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pg_id", nullable = false)
    private Property property;

    @Column(name = "tenant_name", nullable = false)
    private String tenantName;

    @Column(name = "tenant_email", nullable = false)
    private String tenantEmail;

    @Column(name = "tenant_phone", nullable = false)
    private String tenantPhone;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "notes")
    private String notes;

    @Column(nullable = false)
    private String status;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.bookingDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
}
