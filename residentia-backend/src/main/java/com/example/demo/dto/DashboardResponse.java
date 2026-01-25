package com.example.demo.dto;

public record DashboardResponse(	
	long totalUsers,
    long totalOwners,
    long totalProperties,
    long pendingVerifications) {}
