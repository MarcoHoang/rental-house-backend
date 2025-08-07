package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.service.AuthService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource; // Vẫn cần inject MessageSource
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource; // Vẫn inject để truyền vào factory method

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, Locale locale) {
        // Bước 1: Gọi service. Service sẽ trả về một đối tượng LoginResponse hoàn chỉnh.
        LoginResponse loginResponse = authService.login(request);

        // Bước 2: Dòng code "new LoginResponse(token)" đã được xóa bỏ vì không cần thiết nữa.

        // Bước 3: Trả về response thành công với đối tượng đã nhận được.
        return ResponseEntity.ok(
                ApiResponse.success(loginResponse, StatusCode.LOGIN_SUCCESS, messageSource, locale)
        );
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request, Locale locale) {
        authService.register(request);

        // Sử dụng static factory method
        return new ResponseEntity<>(
                ApiResponse.success(StatusCode.REGISTER_SUCCESS, messageSource, locale),
                HttpStatus.CREATED
        );
    }
}