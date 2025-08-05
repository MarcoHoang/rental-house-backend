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
