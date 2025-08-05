package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalDTO;
import com.codegym.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Dùng cho thống kê thu nhập

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RentalController {

    private final RentalService rentalService;

    // Lấy tất cả bản ghi thuê nhà
    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getAll() {
        List<RentalDTO> rentals = rentalService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thuê nhà thành công", rentals));
    }

    // Lấy bản ghi thuê nhà theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> getById(@PathVariable Long id) {
        RentalDTO dto = rentalService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thuê nhà thành công", dto));
    }

    // Tạo mới bản ghi thuê nhà
    @PostMapping
    public ResponseEntity<ApiResponse<RentalDTO>> create(@Valid @RequestBody RentalDTO rentalDTO) {
        RentalDTO created = rentalService.create(rentalDTO);
        return new ResponseEntity<>(new ApiResponse<>(201, "Tạo bản ghi thuê nhà thành công", created), HttpStatus.CREATED);
    }

    // Cập nhật bản ghi thuê nhà
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> update(@PathVariable Long id, @Valid @RequestBody RentalDTO rentalDTO) {
        RentalDTO updated = rentalService.update(id, rentalDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thông tin thuê nhà thành công", updated));
    }

    // Xóa bản ghi thuê nhà (hủy thuê)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rentalService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Hủy thuê nhà thành công", null));
    }

    // Lịch sử thuê nhà của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getUserRentals(@PathVariable Long userId) {
        List<RentalDTO> rentals = rentalService.getUserRentals(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy lịch sử thuê nhà thành công", rentals));
    }

    // Lịch thuê nhà của chủ nhà
    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getHouseRenterRentals(@PathVariable Long landlordId) {
        List<RentalDTO> rentals = rentalService.getHouseRenterRentals(landlordId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy lịch thuê nhà của chủ nhà thành công", rentals));
    }

    // Checkin nhà
    @PutMapping("/{id}/checkin")
    public ResponseEntity<ApiResponse<RentalDTO>> checkin(@PathVariable Long id) {
        RentalDTO dto = rentalService.checkin(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Checkin thành công", dto));
    }

    // Checkout nhà
    @PutMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<RentalDTO>> checkout(@PathVariable Long id) {
        RentalDTO dto = rentalService.checkout(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Checkout thành công", dto));
    }

    // Thống kê thu nhập theo tháng của chủ nhà
    @GetMapping("/landlord/{landlordId}/income")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getHouseRenterIncome(@PathVariable Long landlordId) {
        Map<String, Double> incomeData = rentalService.getHouseRenterIncome(landlordId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Thống kê thu nhập thành công", incomeData));
    }
}