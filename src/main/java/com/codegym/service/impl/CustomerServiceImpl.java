package com.codegym.service.impl;

import com.codegym.dto.response.CustomerDTO;
import com.codegym.entity.User;
import com.codegym.exception.DuplicateEmailException;
import com.codegym.exception.DuplicatePhoneException;
import com.codegym.repository.CustomerRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Giả sử bạn dùng Spring Security
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Quan trọng cho việc mã hóa mật khẩu

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
        if (!user.getPhone().equals(dto.getPhone()) && userRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicatePhoneException("Số điện thoại " + dto.getPhone() + " đã được sử dụng.");
        }
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatar() != null ? dto.getAvatar() : user.getAvatarUrl());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng với ID: " + id));
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateEmailException("Username " + dto.getUsername() + " đã tồn tại.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email " + dto.getEmail() + " đã tồn tại.");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicatePhoneException("Số điện thoại " + dto.getPhone() + " đã tồn tại.");
        }
        User newUser = toEntityForCreate(dto);
        User savedUser = userRepository.save(newUser);
        return toDTO(savedUser);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng với ID: " + id + " để cập nhật."));

        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Không thể xóa. Khách hàng với ID: " + id + " không tồn tại.");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng để đổi mật khẩu."));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public CustomerDTO updateProfile(Long id, CustomerDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng để cập nhật profile."));

        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }
}