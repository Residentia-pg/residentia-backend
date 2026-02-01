package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.demo.entity.RegularUser;
import com.example.demo.repository.RegularUserRepository;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin
public class AdminUserController {
	private final RegularUserRepository userRepository;

	public AdminUserController(RegularUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// getting users
	@GetMapping
	public List<RegularUser> getAllUsers() {
		return userRepository.findAll();
	}

	// deactivate user
	@PutMapping("/{id}/deactivate")
	public void deactivateUser(@PathVariable Integer id) {
		RegularUser user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));

		user.setIsActive(false);
		userRepository.save(user);
	}

	// activate user
	@PutMapping("/{id}/activate")
	public void activateUser(@PathVariable Integer id) {
		RegularUser user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not Found"));
		user.setIsActive(true);
		userRepository.save(user);
	}

	// soft delete
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Integer id) {
		RegularUser user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not Found"));

		user.setIsActive(false);
		userRepository.save(user);
	}

}
