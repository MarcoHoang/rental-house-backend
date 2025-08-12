package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HostDTO;
import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.RentalDTO;
import com.codegym.service.HostService;
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
@RequestMapping("/api/hosts")
@RequiredArgsConstructor
public class HostController {

    private final HostService hostService;
    private final RentalService rentalService;
    private final MessageSource messageSource;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HostDTO>> getHostById(@PathVariable Long id, Locale locale) {
        // Việc kiểm tra người dùng có quyền xem hay không sẽ được xử lý trong service
        HostDTO dto = hostService.getHostById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/my-stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyStats(Locale locale) {
        Map<String, Object> stats = hostService.getCurrentHostStats();
        return ResponseEntity.ok(ApiResponse.success(stats, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HostDTO>> createHost(@RequestBody @Valid HostDTO dto, Locale locale) {
        HostDTO created = hostService.createHost(dto);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HostDTO>> updateHost(@PathVariable Long id, @RequestBody @Valid HostDTO dto, Locale locale) {
        // Việc kiểm tra người dùng có quyền cập nhật hay không sẽ được xử lý trong service
        HostDTO updated = hostService.updateHost(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHost(@PathVariable Long id, Locale locale) {
        hostService.deleteHost(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }

    @PostMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<Void>> lockHost(@PathVariable Long id, Locale locale) {
        hostService.lockHost(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PostMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<Void>> unlockHost(@PathVariable Long id, Locale locale) {
        hostService.unlockHost(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/houses")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getHostHouses(@PathVariable Long id, Locale locale) {
        List<HouseDTO> houses = hostService.getHostHouses(id);
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/rentals")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getHostRentals(@PathVariable Long id, Locale locale) {
        List<RentalDTO> rentals = rentalService.getHostRentals(id);
        return ResponseEntity.ok(ApiResponse.success(rentals, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/income")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getHostIncome(@PathVariable Long id, Locale locale) {
        Map<String, Double> income = rentalService.getHostIncome(id);
        return ResponseEntity.ok(ApiResponse.success(income, StatusCode.SUCCESS, messageSource, locale));
    }
}