package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalDTO;
import com.codegym.service.RentalService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/host/{hostId}/income")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getHostIncome(@PathVariable Long hostId, Locale locale) {
        Map<String, Double> incomeData = rentalService.getHostIncome(hostId);
        return ResponseEntity.ok(ApiResponse.success(incomeData, StatusCode.SUCCESS, messageSource, locale));
    }
}