package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.service.AuthService;
import com.codegym.utils.StatusCode; // Đã có
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        LoginResponse loginResponse = new LoginResponse(token);

        return ResponseEntity.ok(
                new ApiResponse<>(StatusCode.SUCCESS, loginResponse)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return new ResponseEntity<>(
                new ApiResponse<>(StatusCode.SUCCESS),
                HttpStatus.CREATED
        );
    }
}