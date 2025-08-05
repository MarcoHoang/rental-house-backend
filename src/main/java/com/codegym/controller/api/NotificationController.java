package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.NotificationDTO;
import com.codegym.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Lấy danh sách thông báo theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getByUser(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.findByReceiverId(userId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thông báo thành công", notifications));
    }

    // Đánh dấu thông báo đã đọc
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        NotificationDTO dto = notificationService.markAsRead(id);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy thông báo để đánh dấu đã đọc với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Đánh dấu đã đọc thành công", dto));
    }

    // Xóa thông báo
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        notificationService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thông báo thành công", null));
    }

    // Tạo mới thông báo
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> create(@Valid @RequestBody NotificationDTO dto) {
        NotificationDTO created = notificationService.create(dto);
        return ResponseEntity.ok(ApiResponse.success("Tạo thông báo mới thành công", created));
    }
}
