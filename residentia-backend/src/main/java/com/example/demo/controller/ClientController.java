package com.example.demo.controller;

import com.example.demo.entity.Pg;
import com.example.demo.repository.PgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    @Autowired
    private PgRepository pgRepository;

    @GetMapping("/pgs")
    public List<Pg> getAllPgs() {
        // Database madhle sagle PGs client la dakhvnyasathi
        return pgRepository.findAll();
    }
}