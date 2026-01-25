package com.example.demo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Owner;
import com.example.demo.repository.OwnerRepository;
import java.util.List;

@RestController
@RequestMapping("/api/admin/owners")
public class AdminOwnerController 
{
	private final OwnerRepository ownerRepository;
	
	public AdminOwnerController(OwnerRepository ownerRepository)
	{
		this.ownerRepository = ownerRepository;
	}
	
	//getting the owners
	@GetMapping
	public List<Owner> getAllOwners()
	{
		return ownerRepository.findAll();
	}
	
	//set status
	@PutMapping("/{id}/verify")
    public void verifyOwner(@PathVariable Integer id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        owner.setVerificationStatus("VERIFIED");
        owner.setIsActive(true);

        ownerRepository.save(owner);
    }
	
	//reject owner
	@PutMapping("/{id}/reject")
    public void rejectOwner(@PathVariable Integer id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        owner.setVerificationStatus("REJECTED");
        owner.setIsActive(false);

        ownerRepository.save(owner);
    }
	
	//remove owner--soft delete,because still in use
	@DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable Integer id) {
		Owner owner = ownerRepository.findById(id)
		        .orElseThrow(() -> new RuntimeException("Owner not found"));

		    owner.setIsActive(false);
		    owner.setVerificationStatus("REJECTED");

		    ownerRepository.save(owner);
    }
	

}
