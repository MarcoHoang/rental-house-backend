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
    public ResponseEntity<ApiResponse<List<HouseRenterDTO>>> getAllHouseRenters() {
        List<HouseRenterDTO> landlords = houseRenterService.getAllHouseRenters();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách chủ nhà thành công", landlords));
    }

    // Khóa chủ nhà
    @PutMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<Void>> lockHouseRenter(@PathVariable Long id) {
        houseRenterService.lockHouseRenter(id);
        return ResponseEntity.ok(ApiResponse.success("Khóa chủ nhà thành công", null));
    }

    // Mở khóa chủ nhà
    @PutMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<Void>> unlockHouseRenter(@PathVariable Long id) {
        houseRenterService.unlockHouseRenter(id);
        return ResponseEntity.ok(ApiResponse.success("Mở khóa chủ nhà thành công", null));
    }

    // Xem danh sách nhà cho thuê của chủ nhà
    @GetMapping("/{id}/houses")
    public ResponseEntity<ApiResponse<List<Object>>> getHouseRenterHouses(@PathVariable Long id) {
        // Object có thể thay bằng HouseDTO nếu có
        List<Object> houses = houseRenterService.getHouseRenterHouses(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách nhà cho thuê thành công", houses));
    }

    // Lấy landlord theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> getHouseRenterById(@PathVariable Long id) {
        HouseRenterDTO dto = houseRenterService.getHouseRenterById(id);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy chủ nhà với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin chủ nhà thành công", dto));
    }

    // Tạo mới landlord
    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterDTO>> createHouseRenter(@RequestBody HouseRenterDTO dto) {
        HouseRenterDTO created = houseRenterService.createHouseRenter(dto);
        return ResponseEntity.ok(ApiResponse.success("Tạo chủ nhà mới thành công", created));
    }

    // Cập nhật landlord
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> updateHouseRenter(@PathVariable Long id, @RequestBody HouseRenterDTO dto) {
        HouseRenterDTO updated = houseRenterService.updateHouseRenter(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật chủ nhà thành công", updated));
    }

    // Xóa landlord
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouseRenter(@PathVariable Long id) {
        houseRenterService.deleteHouseRenter(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa chủ nhà thành công", null));
    }
}
