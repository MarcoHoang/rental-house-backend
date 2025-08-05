package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.LoginResponse; // DTO mới để chứa token
import com.codegym.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Sử dụng constructor injection, một best practice
@CrossOrigin("*")      // Thêm CrossOrigin để tiện cho việc phát triển với frontend
public class AuthController {

    private final AuthService authService; // Dùng final và @RequiredArgsConstructor

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);

        LoginResponse loginResponse = new LoginResponse(token);

        return ResponseEntity.ok(new ApiResponse<>(200, "Đăng nhập thành công", loginResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return new ResponseEntity<>(
                new ApiResponse<>(201, "Đăng ký tài khoản thành công", null),
                HttpStatus.CREATED
        );
    }
}