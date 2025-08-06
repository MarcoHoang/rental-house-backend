package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalDTO;
import com.codegym.service.RentalService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getAll() {
        List<RentalDTO> rentals = rentalService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, rentals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> getById(@PathVariable Long id) {
        RentalDTO dto = rentalService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RentalDTO>> create(@Valid @RequestBody RentalDTO rentalDTO) {
        RentalDTO created = rentalService.create(rentalDTO);
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.SUCCESS, created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> update(@PathVariable Long id, @Valid @RequestBody RentalDTO rentalDTO) {
        RentalDTO updated = rentalService.update(id, rentalDTO);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rentalService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getUserRentals(@PathVariable Long userId) {
        List<RentalDTO> rentals = rentalService.getUserRentals(userId);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, rentals));
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getHouseRenterRentals(@PathVariable Long landlordId) {
        List<RentalDTO> rentals = rentalService.getHouseRenterRentals(landlordId);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, rentals));
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<ApiResponse<RentalDTO>> checkin(@PathVariable Long id) {
        RentalDTO dto = rentalService.checkin(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<RentalDTO>> checkout(@PathVariable Long id) {
        RentalDTO dto = rentalService.checkout(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @GetMapping("/landlord/{landlordId}/income")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getHouseRenterIncome(@PathVariable Long landlordId) {
        Map<String, Double> incomeData = rentalService.getHouseRenterIncome(landlordId);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, incomeData));
    }
}