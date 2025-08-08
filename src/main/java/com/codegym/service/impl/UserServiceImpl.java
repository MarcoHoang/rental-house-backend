package com.codegym.service.impl;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.UserDTO;
import com.codegym.entity.PasswordResetToken;
import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.PasswordResetTokenRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.EmailService;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    private UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    private User toEntityForCreate(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatarUrl() != null ? dto.getAvatarUrl() : "/images/default-avatar.png");
        user.setActive(dto.isActive());

        if (dto.getRoleName() != null) {
            Role role = new Role();
            role.setName(dto.getRoleName());
            user.setRole(role);
        }

        return user;
    }

    private void updateEntityFromDTO(User user, UserDTO dto) {
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());

        if (dto.getPhone() != null && !user.getPhone().equals(dto.getPhone())) {
            if (userRepository.existsByPhone(dto.getPhone())) {
                throw new AppException(StatusCode.PHONE_ALREADY_EXISTS);
            }
            user.setPhone(dto.getPhone());
        }

        if(dto.getAvatarUrl() != null) {
            user.setAvatarUrl(dto.getAvatarUrl());
        }
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        // 1. Gọi phương thức mới trong repository với Enum RoleName.ADMIN
        Page<User> userPage = userRepository.findByRole_NameNot(RoleName.ADMIN, pageable);

        // 2. Sử dụng hàm .map() có sẵn của Page để chuyển đổi hiệu quả
        return userPage.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = findUserByIdOrThrow(id);
        return toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new AppException(StatusCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AppException(StatusCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new AppException(StatusCode.PHONE_ALREADY_EXISTS);
        }

        User newUser = toEntityForCreate(dto);
        User savedUser = userRepository.save(newUser);
        return toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = findUserByIdOrThrow(id);
        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserByIdOrThrow(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

        if (!currentUser.getId().equals(id)) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION);
        }

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new AppException(StatusCode.INVALID_PASSWORD);
        }

        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            throw new AppException(StatusCode.DUPLICATE_OLD_PASSWORD);
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    @Override
    @Transactional
    public UserDTO updateProfile(Long id, UserDTO dto) {
        User user = findUserByIdOrThrow(id);
        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        passwordResetTokenRepository.save(resetToken);

        emailService.sendResetPasswordEmail(email, token);
    }


    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(StatusCode.TOKEN_INVALID));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new AppException(StatusCode.TOKEN_INVALID);
        }

        User user = resetToken.getUser();

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new AppException(StatusCode.DUPLICATE_OLD_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId));

        if (user.getRole().getName().equals(RoleName.ADMIN)) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Cannot change status of an admin account.");
        }

        user.setActive(active);
        userRepository.save(user);
    }

}