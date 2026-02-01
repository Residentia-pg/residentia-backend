package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class RazorpayController {

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> data) {
        int amount = (int) data.get("amount"); // amount in paise

        Map<String, Object> response = new HashMap<>();
        try {
            // Mock Response for prototype/testing without real keys
            response.put("id", "order_" + System.currentTimeMillis());
            response.put("amount", amount);
            response.put("currency", "INR");
            response.put("status", "created");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
