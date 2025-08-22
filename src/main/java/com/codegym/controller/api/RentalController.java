package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalDTO;
import com.codegym.dto.request.CreateRentalRequest;
import com.codegym.dto.request.RejectRentalRequest;
import com.codegym.dto.request.CancelRentalRequest;
import com.codegym.service.RentalService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RentalController {

    private final RentalService rentalService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getAll(Locale locale) {
        List<RentalDTO> rentals = rentalService.findAll();
        return ResponseEntity.ok(ApiResponse.success(rentals, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> getById(@PathVariable Long id, Locale locale) {
        RentalDTO dto = rentalService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RentalDTO>> create(@Valid @RequestBody RentalDTO rentalDTO, Locale locale) {
        RentalDTO created = rentalService.create(rentalDTO);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> update(@PathVariable Long id, @Valid @RequestBody RentalDTO rentalDTO, Locale locale) {
        RentalDTO updated = rentalService.update(id, rentalDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Locale locale) {
        rentalService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getUserRentals(@PathVariable Long userId, Locale locale) {
        List<RentalDTO> rentals = rentalService.getUserRentals(userId);
        return ResponseEntity.ok(ApiResponse.success(rentals, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/user/me")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getMyRentals(Locale locale) {
        List<RentalDTO> rentals = rentalService.getCurrentUserRentals();
        return ResponseEntity.ok(ApiResponse.success(rentals, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getHostRentals(@PathVariable Long hostId, Locale locale) {
        List<RentalDTO> rentals = rentalService.getHostRentals(hostId);
        return ResponseEntity.ok(ApiResponse.success(rentals, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<ApiResponse<RentalDTO>> checkin(@PathVariable Long id, Locale locale) {
        RentalDTO dto = rentalService.checkin(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<RentalDTO>> checkout(@PathVariable Long id, Locale locale) {
        RentalDTO dto = rentalService.checkout(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<RentalDTO>> cancel(@PathVariable Long id, @Valid @RequestBody CancelRentalRequest request, Locale locale) {
        RentalDTO dto = rentalService.cancel(id, request.getReason());
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/host/{hostId}/income")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getHostIncome(@PathVariable Long hostId, Locale locale) {
        Map<String, Double> incomeData = rentalService.getHostIncome(hostId);
        return ResponseEntity.ok(ApiResponse.success(incomeData, StatusCode.SUCCESS, messageSource, locale));
    }

    // New endpoints for rental request workflow
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<RentalDTO>> createRequest(@Valid @RequestBody CreateRentalRequest request, Locale locale) {
        RentalDTO created = rentalService.createRequest(request);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<RentalDTO>> approveRequest(@PathVariable Long id, Locale locale) {
        RentalDTO approved = rentalService.approveRequest(id);
        return ResponseEntity.ok(ApiResponse.success(approved, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<RentalDTO>> rejectRequest(@PathVariable Long id, @Valid @RequestBody RejectRentalRequest request, Locale locale) {
        RentalDTO rejected = rentalService.rejectRequest(id, request.getReason());
        return ResponseEntity.ok(ApiResponse.success(rejected, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/host/{hostId}/pending")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getHostPendingRequests(@PathVariable Long hostId, Locale locale) {
        List<RentalDTO> requests = rentalService.getHostPendingRequests(hostId);
        return ResponseEntity.ok(ApiResponse.success(requests, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/host/{hostId}/pending/count")
    public ResponseEntity<ApiResponse<Long>> getHostPendingRequestsCount(@PathVariable Long hostId, Locale locale) {
        Long count = rentalService.getHostPendingRequestsCount(hostId);
        return ResponseEntity.ok(ApiResponse.success(count, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/host/me/pending/count")
    public ResponseEntity<ApiResponse<Long>> getMyPendingRequestsCount(Locale locale) {
        Long count = rentalService.getCurrentHostPendingRequestsCount();
        return ResponseEntity.ok(ApiResponse.success(count, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/check-availability")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkAvailability(
            @RequestParam Long houseId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Locale locale) {
        
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        
        boolean isAvailable = !rentalService.existsOverlappingRental(houseId, start, end);
        
        Map<String, Object> result = Map.of(
            "available", isAvailable,
            "message", isAvailable ? "Nhà có thể thuê trong khoảng thời gian này" : "Nhà đã được đặt trong khoảng thời gian này"
        );
        
        return ResponseEntity.ok(ApiResponse.success(result, StatusCode.SUCCESS, messageSource, locale));
    }
}