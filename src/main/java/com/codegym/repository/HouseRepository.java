package com.codegym.repository;

import com.codegym.entity.House;
import com.codegym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByHostId(Long hostId);
    List<House> findByHost(User host);
    
    // Dashboard statistics methods
    long countByStatus(House.Status status);
    
    List<House> findTop5ByOrderByCreatedAtDesc();

    // Search methods
    @Query("SELECT h FROM House h WHERE " +
           "(:keyword IS NULL OR LOWER(h.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(h.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(h.address) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<House> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT h FROM House h WHERE " +
           "(:keyword IS NULL OR LOWER(h.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(h.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(h.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR h.status = :status) " +
           "AND (:houseType IS NULL OR h.houseType = :houseType) " +
           "AND (:hostId IS NULL OR h.host.id = :hostId)")
    List<House> findByAdminFilters(@Param("keyword") String keyword, 
                                  @Param("status") String status, 
                                  @Param("houseType") String houseType, 
                                  @Param("hostId") Long hostId);
}
