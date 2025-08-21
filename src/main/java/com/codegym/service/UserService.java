package com.codegym.service;

import com.codegym.dto.response.HostDTO;
import com.codegym.dto.response.UserDTO;
import com.codegym.dto.response.UserDetailAdminDTO;
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

    void resetPassword(String otp, String newPassword);

    void requestPasswordReset(String email);

    boolean verifyOtp(String otp);

    void updateUserStatus(Long userId, boolean active);

    UserDTO getCurrentUserProfile();

    boolean isCurrentUserHost();

    HostDTO getCurrentUserHostInfo();

    UserDetailAdminDTO findUserDetailById(Long userId);

    // Dashboard statistics methods
    long countAllUsers();
    
    long countHosts();
    
    long countAdmins();

    // Search methods
    List<UserDTO> searchUsers(String keyword, String role, Boolean active);

}