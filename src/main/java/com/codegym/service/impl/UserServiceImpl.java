package com.codegym.service.impl;

import com.codegym.dto.response.UserDTO;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.UserRepository;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setAddress(user.getAddress());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatarUrl());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    private User toEntityForCreate(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatar() != null ? dto.getAvatar() : "/images/default-avatar.png");
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

        if(dto.getAvatar() != null) {
            user.setAvatarUrl(dto.getAvatar());
        }
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
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
}