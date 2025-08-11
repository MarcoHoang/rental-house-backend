package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.UserDTO;
import com.codegym.dto.response.UserDetailAdminDTO;
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

@RestController
@RequestMapping("${api.prefix}/admin/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * Lấy danh sách tất cả người dùng với phân trang và sắp xếp.
     * Phương thức này đã được viết tốt.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            Locale locale) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<UserDTO> userPage = userService.getAllUsers(pageable);

        return ResponseEntity.ok(ApiResponse.success(userPage, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDetailAdminDTO>> getUserDetails(
            @PathVariable Long userId,
            Locale locale) {

        UserDetailAdminDTO userDetail = userService.findUserDetailById(userId);
        // Giả sử bạn có mã StatusCode cho việc lấy chi tiết thành công
        // Nếu chưa có, bạn có thể tạo mới trong enum StatusCode, ví dụ: GET_DETAIL_SUCCESS
        return ResponseEntity.ok(ApiResponse.success(userDetail, StatusCode.SUCCESS, messageSource, locale));
    }

    /**
     * Cập nhật trạng thái active/locked cho một người dùng.
     * Cải tiến bằng cách sử dụng một DTO chuyên dụng thay vì Map.
     */
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody StatusUpdateRequest request,
            Locale locale) {

        userService.updateUserStatus(userId, request.isActive());

        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    /**
     * Lớp DTO nội bộ để nhận request body.
     * Cách làm này giúp code rõ ràng và an toàn hơn về kiểu dữ liệu.
     */
    @lombok.Data
    static class StatusUpdateRequest {
        private boolean active;
    }
}