package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseRenterRequestDTO;
import com.codegym.service.HouseRenterRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlord-requests")
@RequiredArgsConstructor
public class HouseRenterRequestController {

    private final HouseRenterRequestService houseRenterRequestService;

    // Lấy tất cả yêu cầu làm chủ nhà
    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterRequestDTO>>> getAllRequests() {
        List<HouseRenterRequestDTO> requests = houseRenterRequestService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách yêu cầu thành công", requests));
    }

    // Lấy yêu cầu theo userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> getByUserId(@PathVariable Long userId) {
        HouseRenterRequestDTO dto = houseRenterRequestService.findByUserId(userId);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy yêu cầu của người dùng ID = " + userId));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy yêu cầu theo người dùng thành công", dto));
    }

    // Duyệt yêu cầu
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> approve(@PathVariable Long id) {
        HouseRenterRequestDTO dto = houseRenterRequestService.approveRequest(id);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy yêu cầu để duyệt với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Duyệt yêu cầu thành công", dto));
    }

    // Từ chối yêu cầu với lý do
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> reject(@PathVariable Long id, @RequestParam String reason) {
        HouseRenterRequestDTO dto = houseRenterRequestService.rejectRequest(id, reason);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy yêu cầu để từ chối với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Từ chối yêu cầu thành công", dto));
    }
}
