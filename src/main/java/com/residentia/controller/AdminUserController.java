package com.residentia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.residentia.entity.RegularUser;
import com.residentia.repository.RegularUserRepository;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins="*")

public class AdminUserController 
{
	private final RegularUserRepository userRepository;
	
	public AdminUserController(RegularUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	//getting users
	@GetMapping
	public ResponseEntity<List<RegularUser>> getAllUsers() { 
        return ResponseEntity.ok(userRepository.findAll());
    }
	
	//deactivate user
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<RegularUser> deactivateUser(@PathVariable Integer id) { 
        RegularUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        return ResponseEntity.ok(userRepository.save(user));
    }
	
	//activate user
	@PutMapping("/{id}/activate")
	public ResponseEntity<RegularUser> activateUser(@PathVariable Integer id) {
        RegularUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not Found"));
        user.setIsActive(true);
        return ResponseEntity.ok(userRepository.save(user));
    }
	
	//soft delete
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id) { 
        RegularUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not Found"));
        user.setIsActive(false);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
	
}
 