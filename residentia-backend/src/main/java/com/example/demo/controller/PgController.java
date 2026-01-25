package com.example.demo.controller;

import com.example.demo.entity.Owner;
import com.example.demo.entity.Pg;
import com.example.demo.service.OwnerService;
import com.example.demo.service.PgService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/pgs")
@CrossOrigin
public class PgController {

    private final PgService pgService;
    private final OwnerService ownerService;

    public PgController(PgService pgService, OwnerService ownerService) {
        this.pgService = pgService;
        this.ownerService = ownerService;
    }

    private Integer getLoggedOwnerId() {
        return (int) 1L;
    }

    @PostMapping
    public Pg addProperty(@RequestBody Pg pg) {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());
        return pgService.addProperty(pg, owner);
    }

    @GetMapping
    public List<Pg> getProperties() {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());
        return pgService.getOwnerProperties(owner);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        pgService.deleteProperty(id);
    }
}
