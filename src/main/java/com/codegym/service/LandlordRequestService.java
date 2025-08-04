package com.codegym.service;

import com.codegym.dto.response.LandlordRequestDTO;

import java.util.List;

public interface LandlordRequestService {
    List<LandlordRequestDTO> findAll();

    LandlordRequestDTO findByUserId(Long userId);

    LandlordRequestDTO approveRequest(Long id);

    LandlordRequestDTO rejectRequest(Long id, String reason);
}
