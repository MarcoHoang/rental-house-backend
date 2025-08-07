// src/main/java/com/codegym/service/AuthService.java

package com.codegym.service;

import com.codegym.components.JwtTokenUtil;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.mapper.UserMapper;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    // === PHƯƠNG THỨC GÂY LỖI ĐÃ ĐƯỢC SỬA LẠI ===
    // Kiểu trả về đã được đổi từ String -> LoginResponse
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new AppException(StatusCode.INVALID_CREDENTIALS);
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(StatusCode.USER_NOT_FOUND));

        String token = generateTokenSafely(user);

        return createLoginResponse(user, token);
    }

    public LoginResponse adminLogin(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new AppException(StatusCode.INVALID_CREDENTIALS);
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(StatusCode.USER_NOT_FOUND));

        if (!"ADMIN".equalsIgnoreCase(user.getRole().getName().name())) {
            throw new AppException(StatusCode.SUCCESS);
        }

        String token = generateTokenSafely(user);

        return createLoginResponse(user, token);
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(StatusCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(StatusCode.PHONE_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.ROLE_NOT_FOUND));

        User user = userMapper.toEntity(request, role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    // --- Helper Methods ---
    private String generateTokenSafely(User user) {
        try {
            return jwtTokenUtil.generateToken(user);
        } catch (Exception e) {
            log.error("Cannot create JWT Token for user: {}", user.getEmail(), e);
            throw new AppException(StatusCode.TOKEN_INVALID);
        }
    }

    private LoginResponse createLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .token(token)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .build();
    }
}