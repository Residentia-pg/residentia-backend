package com.example.demo.service;

import com.example.demo.entity.Owner;
import com.example.demo.repository.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner getOwner(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
    }

    public Owner updateOwner(Long id, Owner updated) {
        Owner owner = getOwner(id);
        owner.setName(updated.getName());
        owner.setEmail(updated.getEmail());
        owner.setPhone(updated.getPhone());
        owner.setCity(updated.getCity());
        return ownerRepository.save(owner);
    }
}
