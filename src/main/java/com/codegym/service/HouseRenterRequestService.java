package com.codegym.service;

import com.codegym.dto.response.HouseRenterRequestDTO;

import java.util.List;

public interface HouseRenterRequestService {
    List<HouseRenterRequestDTO> findAll();

    HouseRenterRequestDTO findByUserId(Long userId);

    HouseRenterRequestDTO approveRequest(Long id);

    HouseRenterRequestDTO rejectRequest(Long id, String reason);
}
