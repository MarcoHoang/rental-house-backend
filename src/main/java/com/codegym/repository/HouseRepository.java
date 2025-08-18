package com.codegym.repository;

import com.codegym.entity.House;
import com.codegym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByHostId(Long hostId);
    List<House> findByHost(User host);
    
    // Dashboard statistics methods
    long countByStatus(House.Status status);
    
    List<House> findTop5ByOrderByCreatedAtDesc();
}
