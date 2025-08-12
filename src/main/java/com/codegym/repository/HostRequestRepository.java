package com.codegym.repository;

import com.codegym.entity.HostRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostRequestRepository extends JpaRepository<HostRequest, Long> {
    Optional<HostRequest> findByUserId(Long userId);

    boolean existsByUserIdAndStatus(Long userId, HostRequest.Status status);
}

