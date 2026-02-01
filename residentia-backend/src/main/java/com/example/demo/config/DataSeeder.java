package com.example.demo.config;

import com.example.demo.entity.Pg;
import com.example.demo.repository.PgRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataSeeder {

        @Bean
        CommandLineRunner initDatabase(
                        PgRepository pgRepository,
                        com.example.demo.repository.OwnerRepository ownerRepository,
                        com.example.demo.repository.RegularUserRepository userRepository,
                        com.example.demo.repository.PgBookingRepository bookingRepository,
                        com.example.demo.repository.RequestRepository requestRepository) {
                return args -> {
                        // ALWAYS skip seeding - data already exists in database
                        try {
                                long pgCount = pgRepository.count();
                                long userCount = userRepository.count();
                                System.out.println("Database status - PGs: " + pgCount + ", Users: " + userCount
                                                + ". Skipping seeding.");
                                if (pgCount > 0 || userCount > 0) {
                                        return; // Skip seeding if any data exists
                                }
                        } catch (Exception e) {
                                System.out.println("Error checking database, skipping seeding: " + e.getMessage());
                                return;
                        }

                        System.out.println("Seeding high-quality demo data...");

                        Pg pg1 = new Pg();
                        pg1.setName("Elite Living - Kothrud");
                        pg1.setLocation("Kothrud, Pune");
                        pg1.setPrice(12500.0);
                        pg1.setSharingType("2-Sharing");
                        pg1.setCity("Pune");
                        pg1.setState("Maharashtra");
                        pg1.setAddress("Paud Road, Near MIT College, Kothrud");
                        pg1.setPincode("411038");
                        pg1.setAmenities("WiFi,Laundry,AC,Hot Water,Parking,Food");
                        pg1.setMaxCapacity(20);
                        pg1.setAvailableBeds(5);
                        pg1.setFoodIncluded(true);
                        pg1.setImageUrl("https://images.unsplash.com/photo-1522770179533-24471fcdba45");
                        pg1.setDescription(
                                        "Premium PG for students and professionals. Walking distance from MIT College. Features high-speed WiFi and tasty home-cooked meals.");
                        pg1.setOwnerEmail("owner@example.com");
                        pg1.setStatus("ACTIVE");

                        Pg pg2 = new Pg();
                        pg2.setName("SkyView Residency - Viman Nagar");
                        pg2.setLocation("Viman Nagar, Pune");
                        pg2.setPrice(15000.0);
                        pg2.setSharingType("1-Sharing");
                        pg2.setCity("Pune");
                        pg2.setState("Maharashtra");
                        pg2.setAddress("Near Phoenix Mall, Viman Nagar");
                        pg2.setPincode("411014");
                        pg2.setAmenities("WiFi,Laundry,Gym,AC,Hot Water,TV,Food");
                        pg2.setMaxCapacity(15);
                        pg2.setAvailableBeds(3);
                        pg2.setFoodIncluded(true);
                        pg2.setImageUrl("https://images.unsplash.com/photo-1598927480674-15f543834d8a");
                        pg2.setDescription(
                                        "Luxury single-sharing rooms with all modern amenities. Located in the heart of Viman Nagar, close to IT parks and malls.");
                        pg2.setOwnerEmail("owner@example.com");
                        pg2.setStatus("ACTIVE");

                        Pg pg3 = new Pg();
                        pg3.setName("Green Park PG - Baner");
                        pg3.setLocation("Baner, Pune");
                        pg3.setPrice(9500.0);
                        pg3.setSharingType("3-Sharing");
                        pg3.setCity("Pune");
                        pg3.setState("Maharashtra");
                        pg3.setAddress("Baner-Pashan Link Road");
                        pg3.setPincode("411045");
                        pg3.setAmenities("WiFi,Parking,Hot Water,Kitchen");
                        pg3.setMaxCapacity(30);
                        pg3.setAvailableBeds(12);
                        pg3.setFoodIncluded(false);
                        pg3.setImageUrl("https://images.unsplash.com/photo-1560185127-6ed189bf02f4");
                        pg3.setDescription(
                                        "Affordable and spacious rooms in a peaceful locality. Ideal for software professionals working in Baner and Balewadi.");
                        pg3.setOwnerEmail("owner@example.com");
                        pg3.setStatus("ACTIVE");

                        Pg pg4 = new Pg();
                        pg4.setName("The Hive - Hinjewadi");
                        pg4.setLocation("Hinjewadi Phase 1, Pune");
                        pg4.setPrice(11000.0);
                        pg4.setSharingType("2-Sharing");
                        pg4.setCity("Pune");
                        pg4.setState("Maharashtra");
                        pg4.setAddress("Phase 1, Near Infosys Circle");
                        pg4.setPincode("411057");
                        pg4.setAmenities("WiFi,Laundry,Gym,AC,Food");
                        pg4.setMaxCapacity(50);
                        pg4.setAvailableBeds(25);
                        pg4.setFoodIncluded(true);
                        pg4.setImageUrl("https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e");
                        pg4.setDescription(
                                        "Modern co-living space designed for techies. State-of-the-art gym and recreational area included.");
                        pg4.setOwnerEmail("owner@example.com");
                        pg4.setStatus("ACTIVE");

                        Pg pg5 = new Pg();
                        pg5.setName("Urban Nest - Deccan");
                        pg5.setLocation("Deccan Gymkhana, Pune");
                        pg5.setPrice(13500.0);
                        pg5.setSharingType("2-Sharing");
                        pg5.setCity("Pune");
                        pg5.setState("Maharashtra");
                        pg5.setAddress("Prabhat Road, Lane 4");
                        pg5.setPincode("411004");
                        pg5.setAmenities("WiFi,Laundry,Hot Water,TV");
                        pg5.setMaxCapacity(10);
                        pg5.setAvailableBeds(2);
                        pg5.setFoodIncluded(false);
                        pg5.setImageUrl("https://images.unsplash.com/photo-1502672260266-1c1ef2d93688");
                        pg5.setDescription(
                                        "Classic bungalow turned PG in the heritage Prabhat Road area. Very selective and quiet environment.");
                        pg5.setOwnerEmail("owner@example.com");
                        pg5.setStatus("ACTIVE");

                        pgRepository.saveAll(Arrays.asList(pg1, pg2, pg3, pg4, pg5));

                        // 1. Seed Owner
                        System.out.println("Seeding Owner...");
                        com.example.demo.entity.Owner owner = new com.example.demo.entity.Owner();
                        owner.setName("Demo Owner");
                        owner.setEmail("owner@example.com");
                        owner.setMobileNumber("9876543210");
                        owner.setPasswordHash("password"); // In real app, hash this
                        owner.setRole("OWNER");
                        owner.setCity("Pune");
                        owner.setState("Maharashtra");
                        owner.setAddress("123, Owner Street");
                        owner.setIsActive(true);
                        owner.setVerificationStatus("VERIFIED");
                        ownerRepository.save(owner);

                        // 2. Seed Client (RegularUser) - only if not exists
                        System.out.println("Seeding Client...");
                        com.example.demo.entity.RegularUser user = userRepository.findByEmail("client@example.com")
                                        .orElse(null);
                        if (user == null) {
                                user = new com.example.demo.entity.RegularUser();
                                user.setName("Demo Client");
                                user.setEmail("client@example.com");
                                user.setMobileNumber("9123456780");
                                user.setPasswordHash("password");
                                user.setIsActive(true);
                                userRepository.save(user);
                        }

                        // 3. Seed Bookings
                        System.out.println("Seeding Bookings...");
                        com.example.demo.entity.PgBooking booking1 = new com.example.demo.entity.PgBooking();
                        booking1.setUser(user);
                        booking1.setPgId(pg1);
                        booking1.setStartDate(java.time.LocalDate.now().plusDays(1));
                        booking1.setEndDate(java.time.LocalDate.now().plusMonths(6));
                        booking1.setStatus("CONFIRMED");
                        bookingRepository.save(booking1);

                        com.example.demo.entity.PgBooking booking2 = new com.example.demo.entity.PgBooking();
                        booking2.setUser(user);
                        booking2.setPgId(pg2);
                        booking2.setStartDate(java.time.LocalDate.now().plusDays(5));
                        booking2.setEndDate(java.time.LocalDate.now().plusMonths(3));
                        booking2.setStatus("PENDING");
                        bookingRepository.save(booking2);

                        // 4. Seed Change Request
                        System.out.println("Seeding Change Request...");
                        com.example.demo.entity.Request req = new com.example.demo.entity.Request();
                        req.setPg(pg1);
                        req.setOwner(owner);
                        req.setChangeType("UPDATE_PRICE");
                        req.setStatus("PENDING");
                        req.setChangeDetails("{\"price\": 13500}");
                        requestRepository.save(req);

                        System.out.println("Comprehensive Seeding completed successfully!");
                };
        }
}
