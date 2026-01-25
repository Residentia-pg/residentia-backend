package com.example.demo.controller;

import com.example.demo.entity.Owner;
import com.example.demo.service.OwnerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@CrossOrigin
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // Fake logged-in owner (for now)
    private Long getLoggedOwnerId() {
        return 1L;
    }

    @GetMapping("/profile")
    public Owner getProfile() {
        return ownerService.getOwner(getLoggedOwnerId());
    }

    @PutMapping("/profile")
    public Owner updateProfile(@RequestBody Owner owner) {
        return ownerService.updateOwner(getLoggedOwnerId(), owner);
    }
}
