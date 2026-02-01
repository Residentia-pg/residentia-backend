package com.example.demo.service;

import com.example.demo.entity.Owner;
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

    public Pg addProperty(Pg pg, Owner owner) {
        pg.setOwnerEmail(owner.getEmail());
        return pgRepository.save(pg);
    }

    public List<Pg> getOwnerProperties(Owner owner) {
        return pgRepository.findByOwnerEmail(owner.getEmail());
    }

    public void deleteProperty(Integer id) {
        pgRepository.deleteById(id);
    }

    public Pg getById(Integer id) {
        return pgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

}
