package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.service.AuthService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource; // ✅ Import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale; // ✅ Import

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource; // ✅ Inject MessageSource thông qua constructor

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, Locale locale) {
        String token = authService.login(request);
        LoginResponse loginResponse = new LoginResponse(token);

        // Lấy thông báo từ file message
        String message = messageSource.getMessage("auth.login.success", null, locale);

        // Tạo response với mã thành công và thông báo cụ thể
        ApiResponse<LoginResponse> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, loginResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request, Locale locale) {
        authService.register(request);

        // Lấy thông báo từ file message
        String message = messageSource.getMessage("auth.register.success", null, locale);

        // Tạo response với mã thành công và thông báo cụ thể
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}