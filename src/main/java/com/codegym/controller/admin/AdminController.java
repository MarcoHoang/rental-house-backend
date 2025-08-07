 package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.service.AuthService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/admin") // Sử dụng /api/v1/admin
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AuthService authService;
    private final MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> adminLogin(@Valid @RequestBody LoginRequest request, Locale locale) throws Exception {
        // Gọi service để đăng nhập và kiểm tra xem user có phải là admin không
        LoginResponse loginResponse = null;
        try {
            loginResponse = authService.adminLogin(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(
                ApiResponse.success(loginResponse, StatusCode.LOGIN_SUCCESS, messageSource, locale)
        );
    }
}