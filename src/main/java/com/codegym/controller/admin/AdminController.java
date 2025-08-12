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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AuthService authService;
    private final MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> adminLogin(
            @Valid @RequestBody LoginRequest request,
            Locale locale
    ) {
        LoginResponse loginResponse = authService.adminLogin(request);

        return ResponseEntity.ok(
                ApiResponse.success(loginResponse, StatusCode.LOGIN_SUCCESS, messageSource, locale)
        );
    }
}
