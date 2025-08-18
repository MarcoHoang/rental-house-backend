package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TestController {

    private final MessageSource messageSource;

    @GetMapping("/auth-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuthStatus(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> status = new HashMap<>();
        status.put("authenticated", authentication != null && authentication.isAuthenticated());
        status.put("principal", authentication != null ? authentication.getPrincipal() : "anonymous");
        status.put("authorities", authentication != null ? authentication.getAuthorities() : "none");
        status.put("name", authentication != null ? authentication.getName() : "anonymous");
        
        return ResponseEntity.ok(ApiResponse.success(status, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<String>> publicEndpoint(Locale locale) {
        return ResponseEntity.ok(ApiResponse.success("Public endpoint - no auth required", StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/authenticated")
    public ResponseEntity<ApiResponse<String>> authenticatedEndpoint(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String message = "Authenticated endpoint - user: " + authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(message, StatusCode.SUCCESS, messageSource, locale));
    }
}
