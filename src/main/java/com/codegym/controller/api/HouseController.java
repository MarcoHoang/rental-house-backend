package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin("*")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getAllHouses() {
        List<HouseDTO> houses = houseService.getAllHouses();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách nhà thành công", houses));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> searchHouses(@RequestParam(required = false) String keyword) {
        List<HouseDTO> houses = houseService.searchHouses(keyword);
        return ResponseEntity.ok(new ApiResponse<>(200, "Tìm kiếm nhà thành công", houses));
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getTopHouses() {
        List<HouseDTO> houses = houseService.getTopHouses();
        return ResponseEntity.ok(new ApiResponse<>(200, "Top 5 nhà nhiều lượt thuê", houses));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouseStatus(@PathVariable Long id, @RequestParam String status) {
        HouseDTO updated = houseService.updateHouseStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái nhà thành công", updated));
    }

    @GetMapping("/{id}/map")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseMap(@PathVariable Long id) {
        HouseDTO dto = houseService.getHouseById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy vị trí nhà thành công", dto));
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<ApiResponse<List<String>>> getHouseImages(@PathVariable Long id) {
        List<String> images = houseService.getHouseImages(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách ảnh thành công", images));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(@PathVariable Long id) {
        HouseDTO dto = houseService.getHouseById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin nhà thành công", dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseDTO>> createHouse(@RequestBody @Valid HouseDTO dto) {
        HouseDTO created = houseService.createHouse(dto);
        return new ResponseEntity<>(new ApiResponse<>(201, "Tạo nhà mới thành công", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouse(@PathVariable Long id, @RequestBody @Valid HouseDTO dto) {
        HouseDTO updated = houseService.updateHouse(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật nhà thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa nhà thành công", null));
    }
}