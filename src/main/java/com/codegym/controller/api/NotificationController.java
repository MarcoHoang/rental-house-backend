package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.NotificationDTO;
import com.codegym.service.NotificationService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;
    private final MessageSource messageSource;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getByUser(@PathVariable Long userId, Locale locale) {
        List<NotificationDTO> notifications = notificationService.findByReceiverId(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id, Locale locale) {
        NotificationDTO dto = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Locale locale) {
        notificationService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> create(@Valid @RequestBody NotificationDTO dto, Locale locale) {
        NotificationDTO created = notificationService.create(dto);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    // Test endpoint để tạo notification HOUSE_DELETED
    @PostMapping("/test-house-deleted")
    public ResponseEntity<ApiResponse<String>> testHouseDeletedNotification(Locale locale) {
        try {
            // Sử dụng tên nhà thực tế thay vì "Test House"
            String realHouseTitle = "Nhà nguyên căn 3 tầng, full nội thất, trung tâm Hà Nội";
            notificationService.createHouseDeletedNotification(3L, realHouseTitle, 14L);
            return ResponseEntity.ok(ApiResponse.success("Test notification created successfully with title: " + realHouseTitle, StatusCode.CREATED_SUCCESS, messageSource, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(StatusCode.INTERNAL_ERROR.getCode(), "Failed to create test notification: " + e.getMessage(), null));
        }
    }

    // Test endpoint để kiểm tra messageSource
    @GetMapping("/test-message")
    public ResponseEntity<ApiResponse<String>> testMessageSource(Locale locale) {
        try {
            String message = messageSource.getMessage(
                "notification.house.deleted", 
                new Object[]{"Nhà Test"}, 
                locale
            );
            return ResponseEntity.ok(ApiResponse.success("Message: " + message, StatusCode.GET_DETAIL_SUCCESS, messageSource, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(StatusCode.INTERNAL_ERROR.getCode(), "Failed to get message: " + e.getMessage(), null));
        }
    }
}