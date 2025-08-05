package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin("*")
public class HouseController {

    @Autowired
    private HouseService houseService;

    // Lấy tất cả nhà
    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getAllHouses() {
        List<HouseDTO> houses = houseService.getAllHouses();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách nhà thành công", houses));
    }

    // Tìm kiếm, lọc danh sách nhà
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> searchHouses(@RequestParam(required = false) String keyword) {
        List<HouseDTO> houses = houseService.searchHouses(keyword);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm nhà thành công", houses));
    }

    // Top 5 nhà nhiều lượt thuê
    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getTopHouses() {
        List<HouseDTO> houses = houseService.getTopHouses();
        return ResponseEntity.ok(ApiResponse.success("Top 5 nhà nhiều lượt thuê", houses));
    }

    // Cập nhật trạng thái thuê
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouseStatus(@PathVariable Long id, @RequestParam String status) {
        HouseDTO updated = houseService.updateHouseStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái nhà thành công", updated));
    }

    // Xem vị trí trên bản đồ
    @GetMapping("/{id}/map")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseMap(@PathVariable Long id) {
        HouseDTO dto = houseService.getHouseById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy vị trí nhà thành công", dto));
    }

    // Lấy danh sách ảnh của nhà
    @GetMapping("/{id}/images")
    public ResponseEntity<ApiResponse<List<String>>> getHouseImages(@PathVariable Long id) {
        List<String> images = houseService.getHouseImages(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách ảnh thành công", images));
    }

    // Lấy nhà theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(@PathVariable Long id) {
        HouseDTO dto = houseService.getHouseById(id);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy nhà với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin nhà thành công", dto));
    }

    // Tạo nhà mới
    @PostMapping
    public ResponseEntity<ApiResponse<HouseDTO>> createHouse(@RequestBody HouseDTO dto) {
        HouseDTO created = houseService.createHouse(dto);
        return ResponseEntity.ok(ApiResponse.success("Tạo nhà mới thành công", created));
    }

    // Cập nhật thông tin nhà
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouse(@PathVariable Long id, @RequestBody HouseDTO dto) {
        HouseDTO updated = houseService.updateHouse(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật nhà thành công", updated));
    }

    // Xóa nhà
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa nhà thành công", null));
    }
}
