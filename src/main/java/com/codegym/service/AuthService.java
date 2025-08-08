package com.codegym.service;

import com.codegym.components.JwtTokenUtil;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.dto.response.UserDTO;
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
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(StatusCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(StatusCode.INVALID_CREDENTIALS);
        }

        if (!user.isEnabled()) {
            throw new AppException(StatusCode.ACCOUNT_LOCKED);
        }

        try {
            String token = jwtTokenUtil.generateToken(user);
            return new LoginResponse(token);
        } catch (Exception e) {
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }

    }

    public LoginResponse adminLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(StatusCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(StatusCode.INVALID_CREDENTIALS);
        }

        if (!user.getRole().getName().equals(RoleName.ADMIN)) {
            throw new AppException(StatusCode.UNAUTHORIZED);
        }

        try {
            String token = jwtTokenUtil.generateToken(user);
            return new LoginResponse(token);
        } catch (Exception e) {
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    @Transactional
    public UserDTO register(RegisterRequest request) {
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

        return userMapper.toResponse(user);
    }

}