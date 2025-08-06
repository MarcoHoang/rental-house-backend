package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.NotificationDTO;
import com.codegym.service.NotificationService;
import com.codegym.utils.MessageUtil;
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
    private final MessageUtil messageUtil;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getByUser(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.findByReceiverId(userId);
        String message = messageUtil.getMessage("notification.list.byUser.found");
        ApiResponse<List<NotificationDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, notifications);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        NotificationDTO dto = notificationService.markAsRead(id);
        String message = messageUtil.getMessage("notification.markedAsRead");
        ApiResponse<NotificationDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        notificationService.deleteById(id);
        String message = messageUtil.getMessage("notification.deleted");
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.DELETED_SUCCESS.getCode(), message);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> create(@Valid @RequestBody NotificationDTO dto) {
        NotificationDTO created = notificationService.create(dto);
        String message = messageUtil.getMessage("notification.created");
        ApiResponse<NotificationDTO> response = new ApiResponse<>(StatusCode.CREATED_SUCCESS.getCode(), message, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}