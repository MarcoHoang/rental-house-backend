package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.dto.response.UserDTO;
import com.codegym.service.AuthService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * REST controller for handling authentication operations.
 * 
 * This controller provides endpoints for user login, admin login, and user registration.
 * All responses are wrapped in ApiResponse format and support internationalization.
 * 
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    /**
     * Authenticates a user with email and password.
     * 
     * This endpoint validates user credentials and returns a JWT token upon successful authentication.
     * The response includes the token and appropriate success message based on the locale.
     * 
     * @param request The login request containing email and password
     * @param locale The locale for internationalized messages
     * @return ResponseEntity containing the login response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, Locale locale) {
        LoginResponse loginResponse = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(loginResponse, StatusCode.LOGIN_SUCCESS, messageSource, locale)
        );
    }

    /**
     * Registers a new user account.
     * 
     * This endpoint creates a new user account with the provided information.
     * The user is assigned the USER role by default. The response includes
     * the created user's information and appropriate success message.
     * 
     * @param request The registration request containing user information
     * @param locale The locale for internationalized messages
     * @return ResponseEntity containing the created user information with HTTP 201 status
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(
            @Valid @RequestBody RegisterRequest request,
            Locale locale
    ) {
        UserDTO newUser = authService.register(request);

        return new ResponseEntity<>(
                ApiResponse.success(newUser, StatusCode.REGISTER_SUCCESS, messageSource, locale),
                HttpStatus.CREATED
        );
    }
}