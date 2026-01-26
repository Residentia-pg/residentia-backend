package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // He annotation getters/setters auto-generate karel
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private Double rent;
    private String type; // Single/Double sharing
    private String amenities; // WiFi, Food, etc.
}