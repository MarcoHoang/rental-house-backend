
package com.codegym.service.impl;


import com.codegym.dto.response.HouseRenterRequestDTO;
import com.codegym.entity.HouseRenterRequest;
import com.codegym.repository.HouseRenterRequestRepository;
import com.codegym.service.HouseRenterRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseRenterRequestServiceImpl implements HouseRenterRequestService {

    private final HouseRenterRequestRepository houseRenterRequestRepository;

    @Override
    public List<HouseRenterRequestDTO> findAll() {
        return houseRenterRequestRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public HouseRenterRequestDTO findByUserId(Long userId) {
        return houseRenterRequestRepository.findByUserId(userId)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO approveRequest(Long id) {
        HouseRenterRequest request = houseRenterRequestRepository.findById(id).orElseThrow();
        request.setStatus(HouseRenterRequest.Status.APPROVED);
        request.setProcessedDate(java.time.LocalDateTime.now());
        return mapToDTO(houseRenterRequestRepository.save(request));
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO rejectRequest(Long id, String reason) {
        HouseRenterRequest request = houseRenterRequestRepository.findById(id).orElseThrow();
        request.setStatus(HouseRenterRequest.Status.REJECTED);
        request.setReason(reason);
        request.setProcessedDate(java.time.LocalDateTime.now());
        return mapToDTO(houseRenterRequestRepository.save(request));
    }

    private HouseRenterRequestDTO mapToDTO(HouseRenterRequest entity) {
        return HouseRenterRequestDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .userEmail(entity.getUser().getEmail())
                .username(entity.getUser().getUsername())
                .status(entity.getStatus().name())
                .reason(entity.getReason())
                .requestDate(entity.getRequestDate())
                .processedDate(entity.getProcessedDate())
                .build();
    }

    @Override
    public HouseRenterRequestDTO createRequest(HouseRenterRequestDTO dto) {
        // TODO: validate, kiểm tra user đã có yêu cầu chưa, tạo mới entity
        HouseRenterRequest entity = new HouseRenterRequest();
        // Giả sử đã có phương thức setUser, setRequestDate, ...
        // entity.setUser(...);
        // entity.setRequestDate(java.time.LocalDateTime.now());
        // entity.setStatus(HouseRenterRequest.Status.PENDING);
        // entity.setReason(dto.getReason());
        // ...
        return mapToDTO(houseRenterRequestRepository.save(entity));
    }
}
