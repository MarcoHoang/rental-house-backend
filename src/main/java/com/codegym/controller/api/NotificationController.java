package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.NotificationDTO;
import com.codegym.service.NotificationService;
import com.codegym.utils.StatusCode;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getByUser(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.findByReceiverId(userId);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, notifications));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        NotificationDTO dto = notificationService.markAsRead(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        notificationService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> create(@Valid @RequestBody NotificationDTO dto) {
        NotificationDTO created = notificationService.create(dto);
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.SUCCESS, created), HttpStatus.CREATED);
    }
}