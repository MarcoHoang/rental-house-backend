
package com.codegym.service.impl;


import com.codegym.dto.response.CustomerDTO;
import com.codegym.entity.User;
import com.codegym.repository.CustomerRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    private CustomerDTO toDTO(User customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFullName(customer.getFullName());
        dto.setAddress(customer.getAddress());
        dto.setPhone(customer.getPhone());
        dto.setAvatar(customer.getAvatarUrl());
        dto.setUsername(customer.getUsername());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    private User toEntity(CustomerDTO dto, User user) {
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatar() != null ? dto.getAvatar() : "/images/default-avatar.png");
        // Không cập nhật username và email ở đây nếu chúng là thông tin không cho sửa
        return user;
    }


    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User customer = toEntity(dto, user);
        customer.setId(user.getId()); // Vì dùng chung ID với User
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User customer = toEntity(dto, user);
        customer.setId(id); // giữ lại ID gốc
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // Đổi mật khẩu
    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(newPassword); // Nên mã hóa mật khẩu
        userRepository.save(user);
    }

    // Cập nhật profile
    public CustomerDTO updateProfile(Long id, CustomerDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatar());
        userRepository.save(user);
        return toDTO(user);
    }
}
