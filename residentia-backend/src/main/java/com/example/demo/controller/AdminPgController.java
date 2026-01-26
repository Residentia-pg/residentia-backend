package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Pg;
import com.example.demo.repository.PgRepository;

@RestController
@RequestMapping("/api/admin/pgs")
public class AdminPgController {

    private final PgRepository pgRepo;

    public AdminPgController(PgRepository pgRepo) {
        this.pgRepo = pgRepo;
    }

    @GetMapping
    public List<Pg> getAll() {
        return pgRepo.findAll();
    }

    @PutMapping("/{id}/approve")
    public void approve(@PathVariable Integer id) {
        Pg pg = pgRepo.findById(id).orElseThrow();
        pg.setStatus("ACTIVE");
        pgRepo.save(pg);
    }

    @PutMapping("/{id}/reject")
    public void reject(@PathVariable Integer id) {
        Pg pg = pgRepo.findById(id).orElseThrow();
        pg.setStatus("REJECTED");
        pgRepo.save(pg);
    }
}

