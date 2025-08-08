package com.codegym.service;

import com.codegym.dto.response.UserDTO;
import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO createUser(UserDTO dto);

    UserDTO updateUser(Long id, UserDTO dto);

    void deleteUser(Long id);

    void changePassword(Long id, String oldPassword, String newPassword);

    UserDTO updateProfile(Long id, UserDTO dto);

    void resetPassword(String token, String newPassword);

    void requestPasswordReset(String email);
}