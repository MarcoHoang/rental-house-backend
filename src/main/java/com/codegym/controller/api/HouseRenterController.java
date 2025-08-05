package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.service.HouseRenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlords")
@CrossOrigin("*")
public class HouseRenterController {

    @Autowired
    private HouseRenterService houseRenterService;

    // Lấy tất cả landlord
    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterDTO>>> getAllLandlords() {
        List<HouseRenterDTO> landlords = houseRenterService.getAllLandlords();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách chủ nhà thành công", landlords));
    }

    // Lấy landlord theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> getLandlordById(@PathVariable Long id) {
        HouseRenterDTO dto = houseRenterService.getLandlordById(id);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy chủ nhà với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin chủ nhà thành công", dto));
    }

    // Tạo mới landlord
    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterDTO>> createLandlord(@RequestBody HouseRenterDTO dto) {
        HouseRenterDTO created = houseRenterService.createLandlord(dto);
        return ResponseEntity.ok(ApiResponse.success("Tạo chủ nhà mới thành công", created));
    }

    // Cập nhật landlord
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> updateLandlord(@PathVariable Long id, @RequestBody HouseRenterDTO dto) {
        HouseRenterDTO updated = houseRenterService.updateLandlord(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật chủ nhà thành công", updated));
    }

    // Xóa landlord
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLandlord(@PathVariable Long id) {
        houseRenterService.deleteLandlord(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa chủ nhà thành công", null));
    }
}
