package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DashboardResponse;
import com.example.demo.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

	private final AdminDashboardService service;
	public AdminDashboardController(AdminDashboardService service) {
	    this.service = service;
	}
	
	@GetMapping
	public DashboardResponse getDashboard()
	{
		return service.getDashboardData();
	}
}
