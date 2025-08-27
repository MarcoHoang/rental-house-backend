package com.codegym.repository;

import com.codegym.entity.HostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HostRequestRepository extends JpaRepository<HostRequest, Long> {
    Optional<HostRequest> findByUserId(Long userId);

    boolean existsByUserIdAndStatus(Long userId, HostRequest.Status status);

    Page<HostRequest> findByStatus(HostRequest.Status status, Pageable pageable);

    @Query("SELECT hr FROM HostRequest hr WHERE " +
           "(:keyword IS NULL OR LOWER(hr.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(hr.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(hr.user.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(hr.user.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR hr.status = :status)")
    Page<HostRequest> findByKeywordAndStatus(@Param("keyword") String keyword,
                                             @Param("status") HostRequest.Status status,
                                             Pageable pageable);

    @Query("SELECT hr FROM HostRequest hr WHERE " +
           "(:status IS NULL OR hr.status = :status)")
    Page<HostRequest> findByStatusOnly(@Param("status") HostRequest.Status status,
                                       Pageable pageable);
}


