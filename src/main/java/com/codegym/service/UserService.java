package com.codegym.service;

import com.codegym.dto.response.UserDTO;
import java.util.List;

public interface UserService {

    List<UserDTO> getAllCustomers();

    UserDTO getCustomerById(Long id);

    UserDTO createCustomer(UserDTO dto);

    UserDTO updateCustomer(Long id, UserDTO dto);

    void deleteCustomer(Long id);

    void changePassword(Long id, String newPassword);

    UserDTO updateProfile(Long id, UserDTO dto);
}