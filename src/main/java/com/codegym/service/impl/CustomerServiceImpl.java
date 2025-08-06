package com.codegym.service.impl;

import com.codegym.dto.response.CustomerDTO;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.CustomerRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.CustomerService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private CustomerDTO toDTO(User user) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setAddress(user.getAddress());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatarUrl());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    private User toEntityForCreate(CustomerDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatar() != null ? dto.getAvatar() : "/images/default-avatar.png");
        return user;
    }

    private void updateEntityFromDTO(User user, CustomerDTO dto) {
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
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        User user = findUserByIdOrThrow(id);
        return toDTO(user);
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO dto) {
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
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        User user = findUserByIdOrThrow(id);
        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        User user = findUserByIdOrThrow(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = findUserByIdOrThrow(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public CustomerDTO updateProfile(Long id, CustomerDTO dto) {
        User user = findUserByIdOrThrow(id);
        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }
}