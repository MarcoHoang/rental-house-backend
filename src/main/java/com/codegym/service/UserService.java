package com.codegym.service;

import com.codegym.dto.response.UserDTO;
import com.codegym.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllCustomers();

    UserDTO getCustomerById(Long id);

    UserDTO createCustomer(UserDTO dto);

    UserDTO updateCustomer(Long id, UserDTO dto);

    void deleteCustomer(Long id);

    void changePassword(Long id, String newPassword);

    UserDTO updateProfile(Long id, UserDTO dto);

    Page<UserResponse> findAllUsers(Pageable pageable);
    void updateUserStatus(Long userId, boolean active);
}