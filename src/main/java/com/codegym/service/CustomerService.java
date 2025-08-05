package com.codegym.service;

import com.codegym.dto.response.CustomerDTO;
import java.util.List;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(Long id);

    CustomerDTO createCustomer(CustomerDTO dto);

    CustomerDTO updateCustomer(Long id, CustomerDTO dto);

    void deleteCustomer(Long id);

    void changePassword(Long id, String newPassword);

    CustomerDTO updateProfile(Long id, CustomerDTO dto);
}