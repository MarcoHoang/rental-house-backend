package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalDTO;
import com.codegym.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin("*")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    // Lấy tất cả bản ghi thuê nhà
    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getAll() {
        List<RentalDTO> rentals = rentalService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thuê nhà thành công", rentals));
    }

    // Lấy bản ghi thuê nhà theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> getById(@PathVariable Long id) {
        RentalDTO dto = rentalService.findById(id);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy bản ghi thuê nhà với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thuê nhà thành công", dto));
    }

    // Tạo mới bản ghi thuê nhà
    @PostMapping
    public ResponseEntity<ApiResponse<RentalDTO>> create(@RequestBody RentalDTO rentalDTO) {
        RentalDTO created = rentalService.create(rentalDTO);
        return ResponseEntity.ok(ApiResponse.success("Tạo bản ghi thuê nhà thành công", created));
    }

    // Cập nhật bản ghi thuê nhà
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> update(@PathVariable Long id, @RequestBody RentalDTO rentalDTO) {
        RentalDTO updated = rentalService.update(id, rentalDTO);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin thuê nhà thành công", updated));
    }

    // Xóa bản ghi thuê nhà
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rentalService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa bản ghi thuê nhà thành công", null));
    }
}
