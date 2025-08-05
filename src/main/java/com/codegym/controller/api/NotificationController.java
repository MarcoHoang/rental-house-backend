package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.NotificationDTO;
import com.codegym.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;

    // Lấy danh sách thông báo theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getByUser(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.findByReceiverId(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thông báo thành công", notifications));
    }

    // Đánh dấu thông báo đã đọc
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        NotificationDTO dto = notificationService.markAsRead(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Đánh dấu đã đọc thành công", dto));
    }

    // Xóa thông báo
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        notificationService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa thông báo thành công", null));
    }

    // Tạo mới thông báo
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> create(@Valid @RequestBody NotificationDTO dto) {
        NotificationDTO created = notificationService.create(dto);
        return new ResponseEntity<>(new ApiResponse<>(201, "Tạo thông báo mới thành công", created), HttpStatus.CREATED);
    }
}