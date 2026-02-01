package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.demo.entity.Request;
import com.example.demo.repository.RequestRepository;

@RestController
@RequestMapping("/api/admin/change-requests")
@CrossOrigin
public class AdminRequestController {

    private final RequestRepository repo;

    public AdminRequestController(RequestRepository repo) {
        this.repo = repo;
    }

    // getting all requests
    @GetMapping
    public List<Request> getAll() {
        return repo.findAll();
    }

    // Approve request
    @PutMapping("/{id}/approve")
    public Request approve(@PathVariable Integer id) {
        Request r = repo.findById(id).orElseThrow();
        r.setStatus("APPROVED");
        return repo.save(r);
    }

    // 3️⃣ Reject request
    @PutMapping("/{id}/reject")
    public Request reject(@PathVariable Integer id) {
        Request r = repo.findById(id).orElseThrow();
        r.setStatus("REJECTED");
        return repo.save(r);
    }
}
