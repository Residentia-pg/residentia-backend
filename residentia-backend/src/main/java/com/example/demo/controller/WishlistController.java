package com.example.demo.controller;

import com.example.demo.entity.Pg;
import com.example.demo.entity.RegularUser;
import com.example.demo.entity.Wishlist;
import com.example.demo.repository.PgRepository;
import com.example.demo.repository.RegularUserRepository;
import com.example.demo.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private RegularUserRepository userRepository;

    @Autowired
    private PgRepository pgRepository;

    @PostMapping("/toggle")
    @Transactional
    public ResponseEntity<?> toggleWishlist(@RequestBody Map<String, Integer> data) {
        Integer userId = data.get("userId");
        Integer pgId = data.get("pgId");

        RegularUser user = userRepository.findById(userId).orElse(null);
        Pg pg = pgRepository.findById(pgId).orElse(null);

        if (user == null || pg == null) {
            return ResponseEntity.badRequest().body("User or PG not found");
        }

        Optional<Wishlist> existing = wishlistRepository.findByUserAndPg(user, pg);
        if (existing.isPresent()) {
            wishlistRepository.deleteByUserAndPg(user, pg);
            return ResponseEntity.ok(Map.of("status", "removed", "pgId", pgId));
        } else {
            wishlistRepository.save(new Wishlist(user, pg));
            return ResponseEntity.ok(Map.of("status", "added", "pgId", pgId));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserWishlist(@PathVariable Integer userId) {
        RegularUser user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        List<Wishlist> wishlist = wishlistRepository.findByUser(user);
        List<Integer> pgIds = wishlist.stream()
                .map(w -> w.getPg().getId())
                .collect(Collectors.toList());

        return ResponseEntity.ok(pgIds);
    }
}
