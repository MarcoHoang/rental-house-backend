package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.UserDTO;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/admin/users") // <-- Tất cả các API trong đây sẽ bắt đầu bằng /api/admin/users
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminUserController {

    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(Pageable pageable, Locale locale) {
        Page<UserDTO> usersPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(usersPage, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request, // Nhận trạng thái mới từ body
            Locale locale) {
        userService.updateUserStatus(id, request.isActive());
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @lombok.Data
    static class StatusUpdateRequest {
        private boolean active;
    }
}