package com.codegym.service.impl;

import com.codegym.dto.response.LandlordRequestDTO;
import com.codegym.entity.LandlordRequest;
import com.codegym.repository.LandlordRequestRepository;
import com.codegym.service.LandlordRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LandlordRequestServiceImpl implements LandlordRequestService {

    private final LandlordRequestRepository landlordRequestRepository;

    @Override
    public List<LandlordRequestDTO> findAll() {
        return landlordRequestRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public LandlordRequestDTO findByUserId(Long userId) {
        return landlordRequestRepository.findByUserId(userId)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public LandlordRequestDTO approveRequest(Long id) {
        LandlordRequest request = landlordRequestRepository.findById(id).orElseThrow();
        request.setStatus(LandlordRequest.Status.APPROVED);
        request.setProcessedDate(java.time.LocalDateTime.now());
        return mapToDTO(landlordRequestRepository.save(request));
    }

    @Override
    @Transactional
    public LandlordRequestDTO rejectRequest(Long id, String reason) {
        LandlordRequest request = landlordRequestRepository.findById(id).orElseThrow();
        request.setStatus(LandlordRequest.Status.REJECTED);
        request.setReason(reason);
        request.setProcessedDate(java.time.LocalDateTime.now());
        return mapToDTO(landlordRequestRepository.save(request));
    }

    private LandlordRequestDTO mapToDTO(LandlordRequest entity) {
        return LandlordRequestDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .userEmail(entity.getUser().getEmail())
                .userName(entity.getUser().getFullName())
                .status(entity.getStatus().name())
                .reason(entity.getReason())
                .requestDate(entity.getRequestDate())
                .processedDate(entity.getProcessedDate())
                .build();
    }
}
