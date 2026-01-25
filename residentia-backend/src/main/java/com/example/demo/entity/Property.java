package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private double price;
    private int totalRooms;
    private String sharingType;
    private String description;

    private boolean wifi;
    private boolean ac;
    private boolean food;
    private boolean laundry;
    private boolean parking;

    @ManyToOne
    @JsonIgnoreProperties({"properties", "bookings"})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Owner owner;


    // Getters & Setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }

    public String getSharingType() { return sharingType; }
    public void setSharingType(String sharingType) { this.sharingType = sharingType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isWifi() { return wifi; }
    public void setWifi(boolean wifi) { this.wifi = wifi; }

    public boolean isAc() { return ac; }
    public void setAc(boolean ac) { this.ac = ac; }

    public boolean isFood() { return food; }
    public void setFood(boolean food) { this.food = food; }

    public boolean isLaundry() { return laundry; }
    public void setLaundry(boolean laundry) { this.laundry = laundry; }

    public boolean isParking() { return parking; }
    public void setParking(boolean parking) { this.parking = parking; }

    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }
}
