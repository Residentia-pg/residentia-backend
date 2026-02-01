package com.example.demo.controller;

import com.example.demo.entity.Owner;
import com.example.demo.service.PgBookingService;
import com.example.demo.service.DashboardService;
import com.example.demo.service.OwnerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner/dashboard")
@CrossOrigin
public class DashboardController {

    private final OwnerService ownerService;
    private final DashboardService dashboardService;
    private final PgBookingService pgBookingService;

    public DashboardController(OwnerService ownerService,
            DashboardService dashboardService,
            PgBookingService pgBookingService) {
        this.ownerService = ownerService;
        this.dashboardService = dashboardService;
        this.pgBookingService = pgBookingService;
    }

    private Integer getLoggedOwnerId() {
        return (int) 1L;
    }

    @GetMapping
    public Object getDashboard() {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());

        return new Object() {
            public long totalProperties = dashboardService.getTotalProperties(owner);
            public long totalBookings = dashboardService.getTotalBookings(owner);
            public Object recentBookings = pgBookingService.getOwnerBookings(owner);
        };
    }
}
