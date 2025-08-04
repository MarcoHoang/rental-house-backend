package com.codegym.repository;

import com.codegym.entity.LandlordRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandlordRequestRepository extends JpaRepository<LandlordRequest, Long> {
    Optional<LandlordRequest> findByUserId(Long userId);
}
