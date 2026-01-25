package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.DashboardResponse;
import com.example.demo.repository.OwnerRepository;
import com.example.demo.repository.PgRepository;
import com.example.demo.repository.RegularUserRepository;

@Service
public class AdminDashboardService 
{
	private final RegularUserRepository userRepo;
    private final OwnerRepository ownerRepo;
    private final PgRepository pgRepo;
    
    public AdminDashboardService(
            RegularUserRepository userRepo,
            OwnerRepository ownerRepo,
            PgRepository pgRepo) 
    {

            this.userRepo = userRepo;
            this.ownerRepo = ownerRepo;
            this.pgRepo = pgRepo;
    }
    
    public DashboardResponse getDashboardData() {

        return new DashboardResponse(
            userRepo.count(),
            ownerRepo.count(),
            pgRepo.count(),
            ownerRepo.countByVerificationStatus("PENDING")
        );
    }
}
