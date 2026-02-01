package com.example.demo.repository;

import com.example.demo.entity.Pg;
import com.example.demo.entity.RegularUser;
import com.example.demo.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    List<Wishlist> findByUser(RegularUser user);

    Optional<Wishlist> findByUserAndPg(RegularUser user, Pg pg);

    void deleteByUserAndPg(RegularUser user, Pg pg);
}
