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
import com.codegym.repository.UserRepository;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;
    private final MessageSource messageSource;
    private final UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getByUser(@PathVariable Long userId, Locale locale) {
        List<NotificationDTO> notifications = notificationService.findByReceiverId(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/my-notifications")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getMyNotifications(Locale locale) {
        // Lấy user hiện tại từ SecurityContext
        String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        com.codegym.entity.User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new com.codegym.exception.ResourceNotFoundException(com.codegym.utils.StatusCode.USER_NOT_FOUND, currentUserEmail));
        
        List<NotificationDTO> notifications = notificationService.findByReceiverId(currentUser.getId());
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


}