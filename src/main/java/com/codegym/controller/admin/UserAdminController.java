package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.UserResponse;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/admin/users")
@CrossOrigin("*") // Thêm để đồng nhất với UserController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;
    private final MessageSource messageSource; // Thêm MessageSource để xử lý thông điệp

    /**
     * Lấy danh sách tất cả người dùng với phân trang và sắp xếp.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            Locale locale) { // Thêm Locale để lấy message đúng ngôn ngữ

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<UserResponse> userPage = userService.findAllUsers(pageable);

        // Đóng gói response bằng ApiResponse
        return ResponseEntity.ok(ApiResponse.success(userPage, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    /**
     * Cập nhật trạng thái active/locked cho một người dùng.
     */
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody Map<String, Boolean> statusUpdate,
            Locale locale) { // Thêm Locale

        // Lấy giá trị 'active' từ request body, nếu không có thì mặc định là false
        boolean isActive = statusUpdate.getOrDefault("active", false);

        userService.updateUserStatus(userId, isActive);

        // Đóng gói response bằng ApiResponse
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }
}