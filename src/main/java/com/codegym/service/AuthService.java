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

/**
 * Service for handling user authentication and registration operations.
 * 
 * This service provides methods for user login, admin login, and user registration.
 * It handles password encryption, JWT token generation, and user validation.
 * 
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Authenticates a user with email and password, returning a JWT token.
     * 
     * This method validates user credentials, checks if the account is active,
     * and generates a JWT token for successful authentication.
     * 
     * @param request The login request containing email and password
     * @return LoginResponse containing the JWT token
     * @throws AppException if credentials are invalid or account is locked
     */
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
            log.error("Error generating JWT token for user: {}", user.getEmail(), e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    /**
     * Authenticates an admin user with email and password, returning a JWT token.
     * 
     * This method validates admin credentials and ensures the user has ADMIN role.
     * 
     * @param request The login request containing email and password
     * @return LoginResponse containing the JWT token
     * @throws AppException if credentials are invalid or user is not an admin
     */
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
            log.error("Error generating JWT token for admin: {}", user.getEmail(), e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    /**
     * Registers a new user with the provided information.
     * 
     * This method validates that the email and phone are unique, creates a new user
     * with USER role, encrypts the password, and saves the user to the database.
     * 
     * @param request The registration request containing user information
     * @return UserDTO containing the created user's information
     * @throws AppException if email or phone already exists
     * @throws ResourceNotFoundException if USER role is not found
     */
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

        User savedUser = userRepository.save(user);
        log.info("New user registered: {}", savedUser.getEmail());

        return userMapper.toResponse(savedUser);
    }
}