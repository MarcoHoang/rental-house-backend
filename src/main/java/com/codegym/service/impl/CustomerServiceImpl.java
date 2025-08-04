package com.codegym.service.impl;

import com.codegym.dto.response.CustomerDTO;
import com.codegym.entity.Customer;
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

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFullName(customer.getFullName());
        dto.setAddress(customer.getAddress());
        dto.setPhone(customer.getPhone());
        dto.setAvatar(customer.getAvatar());
        dto.setUsername(customer.getUser().getUsername());
        dto.setEmail(customer.getUser().getEmail());
        return dto;
    }

    private Customer toEntity(CustomerDTO dto, User user) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setUser(user);
        customer.setFullName(dto.getFullName());
        customer.setAddress(dto.getAddress());
        customer.setPhone(dto.getPhone());
        customer.setAvatar(dto.getAvatar() != null ? dto.getAvatar() : "/images/default-avatar.png");
        return customer;
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
        Customer customer = toEntity(dto, user);
        customer.setId(user.getId()); // Vì dùng chung ID với User
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = toEntity(dto, user);
        customer.setId(id); // giữ lại ID gốc
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
