package com.codegym.service;

import com.codegym.dto.request.CreateRentalRequest;
import com.codegym.dto.response.RentalDTO;
import com.codegym.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RentalService {

    List<RentalDTO> findAll();

    RentalDTO findById(Long id);

    RentalDTO create(RentalDTO dto);

    RentalDTO update(Long id, RentalDTO dto);

    void delete(Long id);

    RentalDTO checkin(Long id);

    RentalDTO checkout(Long id);

    RentalDTO cancel(Long id, String reason);

    List<RentalDTO> getUserRentals(Long userId);

    List<RentalDTO> getCurrentUserRentals();

    List<RentalDTO> getHostRentals(Long hostId);

    Map<String, Double> getHostIncome(Long hostId);

    // New methods for rental request workflow
    RentalDTO createRequest(CreateRentalRequest request);
    
    RentalDTO approveRequest(Long rentalId);
    
    RentalDTO rejectRequest(Long rentalId, String reason);
    
    List<RentalDTO> getHostPendingRequests(Long hostId);
    
    List<RentalDTO> getHostRequestsByStatus(Long hostId, List<Rental.Status> statuses);
    
    List<RentalDTO> getUserRequestsByStatus(Long userId, List<Rental.Status> statuses);
    
    Long getHostPendingRequestsCount(Long hostId);
    
    boolean existsOverlappingRental(Long houseId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    // Dashboard statistics methods
    long countAllRentals();
    
    long countRentalsByStatus(String status);
    
    double calculateTotalRevenue();
    
    double calculateMonthlyRevenue();
    
    List<Map<String, Object>> getRecentRentalsForDashboard(int limit);

    // Admin methods
    Page<RentalDTO> getAllRentalsForAdmin(Pageable pageable);
    
    Page<RentalDTO> searchRentalsForAdmin(String keyword, String status, String houseType, Pageable pageable);
    
    RentalDTO getRentalByIdForAdmin(Long id);

}