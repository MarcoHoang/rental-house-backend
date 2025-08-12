package com.codegym.service;

import com.codegym.dto.response.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getUserById(Long id);

    UserDTO createUser(UserDTO dto);

    UserDTO updateUser(Long id, UserDTO dto);

    void deleteUser(Long id);

    void changePassword(Long id, String oldPassword, String newPassword, String confirmPassword);

    UserDTO updateProfile(Long id, UserDTO dto);

    void resetPassword(String token, String newPassword);

    void requestPasswordReset(String email);

    void updateUserStatus(Long userId, boolean active);

    UserDTO getCurrentUserProfile();
}