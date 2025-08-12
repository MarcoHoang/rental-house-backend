package com.codegym.service;

import com.codegym.dto.response.HostRequestDTO;
import java.util.List;

public interface HostRequestService {

    List<HostRequestDTO> findAll();

    HostRequestDTO findByUserId(Long userId);

    HostRequestDTO getCurrentUserRequest();

    HostRequestDTO createRequest(HostRequestDTO dto);

    HostRequestDTO approveRequest(Long id);

    HostRequestDTO rejectRequest(Long id, String reason);
}

