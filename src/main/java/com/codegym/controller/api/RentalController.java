package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalDTO;
import com.codegym.service.RentalService;
import com.codegym.utils.MessageUtil;
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
    private final MessageUtil messageUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getAll() {
        List<RentalDTO> rentals = rentalService.findAll();
        String message = messageUtil.getMessage("rental.list.found");
        ApiResponse<List<RentalDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, rentals);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> getById(@PathVariable Long id) {
        RentalDTO dto = rentalService.findById(id);
        String message = messageUtil.getMessage("rental.get.found");
        ApiResponse<RentalDTO> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RentalDTO>> create(@Valid @RequestBody RentalDTO rentalDTO) {
        RentalDTO created = rentalService.create(rentalDTO);
        String message = messageUtil.getMessage("rental.created");
        ApiResponse<RentalDTO> response = new ApiResponse<>(StatusCode.CREATED_SUCCESS.getCode(), message, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> update(@PathVariable Long id, @Valid @RequestBody RentalDTO rentalDTO) {
        RentalDTO updated = rentalService.update(id, rentalDTO);
        String message = messageUtil.getMessage("rental.updated");
        ApiResponse<RentalDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rentalService.delete(id);
        String message = messageUtil.getMessage("rental.deleted");
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.DELETED_SUCCESS.getCode(), message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getUserRentals(@PathVariable Long userId) {
        List<RentalDTO> rentals = rentalService.getUserRentals(userId);
        String message = messageUtil.getMessage("rental.list.byUser.found");
        ApiResponse<List<RentalDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, rentals);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getHouseRenterRentals(@PathVariable Long landlordId) {
        List<RentalDTO> rentals = rentalService.getHouseRenterRentals(landlordId);
        String message = messageUtil.getMessage("rental.list.byLandlord.found");
        ApiResponse<List<RentalDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, rentals);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<ApiResponse<RentalDTO>> checkin(@PathVariable Long id) {
        RentalDTO dto = rentalService.checkin(id);
        String message = messageUtil.getMessage("rental.checkedIn");
        ApiResponse<RentalDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<RentalDTO>> checkout(@PathVariable Long id) {
        RentalDTO dto = rentalService.checkout(id);
        String message = messageUtil.getMessage("rental.checkedOut");
        ApiResponse<RentalDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/landlord/{landlordId}/income")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getHouseRenterIncome(@PathVariable Long landlordId) {
        Map<String, Double> incomeData = rentalService.getHouseRenterIncome(landlordId);
        String message = messageUtil.getMessage("rental.income.found");
        ApiResponse<Map<String, Double>> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, incomeData);
        return ResponseEntity.ok(response);
    }
}