package com.example.demo.service;

import com.example.demo.entity.Pg;
import com.example.demo.repository.PgRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PgService {

    private final PgRepository pgRepository;

    public PgService(PgRepository pgRepository) {
        this.pgRepository = pgRepository;
    }

    // This fetches ALL properties from the database for the Client
    public List<Pg> getAllPgs() {
        return pgRepository.findAll();
    }
}