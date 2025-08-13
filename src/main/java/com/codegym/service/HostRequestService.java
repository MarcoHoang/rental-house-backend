package com.codegym.service;

import com.codegym.dto.response.HostRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HostRequestService {

    Page<HostRequestDTO> findAll(Pageable pageable);

    Page<HostRequestDTO> findPending(Pageable pageable);

    HostRequestDTO findByUserId(Long userId);

    HostRequestDTO getCurrentUserRequest();

    HostRequestDTO createRequest(HostRequestDTO dto);

    HostRequestDTO approveRequest(Long id);

    HostRequestDTO rejectRequest(Long id, String reason);

    HostRequestDTO findById(Long id);
}

