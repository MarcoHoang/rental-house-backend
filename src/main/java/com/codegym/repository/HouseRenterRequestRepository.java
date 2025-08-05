package com.codegym.repository;

import com.codegym.entity.HouseRenterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseRenterRequestRepository extends JpaRepository<HouseRenterRequest, Long> {
    Optional<HouseRenterRequest> findByUserId(Long userId);

    boolean existsByUserIdAndStatus(Long userId, HouseRenterRequest.Status status);
}
