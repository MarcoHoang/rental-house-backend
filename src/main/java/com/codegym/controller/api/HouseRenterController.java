package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO; // Giả định HouseRenterHouses sẽ trả về List<HouseDTO>
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.service.HouseRenterService;
import jakarta.validation.Valid; // Quan trọng: import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlords")
@CrossOrigin("*")
public class HouseRenterController {

    @Autowired
    private HouseRenterService houseRenterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterDTO>>> getAllHouseRenters() {
        List<HouseRenterDTO> landlords = houseRenterService.getAllHouseRenters();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách chủ nhà thành công", landlords));
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<Void>> lockHouseRenter(@PathVariable Long id) {
        houseRenterService.lockHouseRenter(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Khóa chủ nhà thành công", null));
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<Void>> unlockHouseRenter(@PathVariable Long id) {
        houseRenterService.unlockHouseRenter(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Mở khóa chủ nhà thành công", null));
    }

    @GetMapping("/{id}/houses")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getHouseRenterHouses(@PathVariable Long id) {
        // Thay đổi Object thành HouseDTO cho rõ ràng và nhất quán
        List<HouseDTO> houses = houseRenterService.getHouseRenterHouses(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách nhà cho thuê thành công", houses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> getHouseRenterById(@PathVariable Long id) {
        // Loại bỏ kiểm tra null, service sẽ ném EntityNotFoundException
        HouseRenterDTO dto = houseRenterService.getHouseRenterById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin chủ nhà thành công", dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterDTO>> createHouseRenter(@RequestBody @Valid HouseRenterDTO dto) {
        HouseRenterDTO created = houseRenterService.createHouseRenter(dto);
        // Trả về status 201 CREATED thì sẽ chuẩn RESTful hơn
        return new ResponseEntity<>(new ApiResponse<>(201, "Tạo chủ nhà mới thành công", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> updateHouseRenter(@PathVariable Long id, @RequestBody @Valid HouseRenterDTO dto) {
        HouseRenterDTO updated = houseRenterService.updateHouseRenter(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật chủ nhà thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouseRenter(@PathVariable Long id) {
        houseRenterService.deleteHouseRenter(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa chủ nhà thành công", null));
    }
}