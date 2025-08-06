package com.codegym.service.impl;

import com.codegym.dto.response.HouseRenterRequestDTO;
import com.codegym.entity.HouseRenter;
import com.codegym.entity.HouseRenterRequest;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRenterRepository;
import com.codegym.repository.HouseRenterRequestRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseRenterRequestService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseRenterRequestServiceImpl implements HouseRenterRequestService {

    private final HouseRenterRequestRepository houseRenterRequestRepository;
    private final HouseRenterRepository houseRenterRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HouseRenterRequestDTO> findAll() {
        return houseRenterRequestRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseRenterRequestDTO findByUserId(Long userId) {
        return houseRenterRequestRepository.findByUserId(userId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.REQUEST_NOT_FOUND, userId));
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO createRequest(HouseRenterRequestDTO dto) {
        Long userId = dto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId));

        if (houseRenterRepository.existsById(userId)) {
            throw new AppException(StatusCode.USER_ALREADY_HOUSE_RENTER);
        }
        if (houseRenterRequestRepository.existsByUserIdAndStatus(userId, HouseRenterRequest.Status.PENDING)) {
            throw new AppException(StatusCode.PENDING_REQUEST_EXISTS);
        }

        HouseRenterRequest entity = new HouseRenterRequest();
        entity.setUser(user);
        entity.setRequestDate(LocalDateTime.now());
        entity.setStatus(HouseRenterRequest.Status.PENDING);

        HouseRenterRequest savedEntity = houseRenterRequestRepository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO approveRequest(Long id) {
        HouseRenterRequest request = findRequestByIdOrThrow(id);

        if (request.getStatus() != HouseRenterRequest.Status.PENDING) {
            throw new AppException(StatusCode.INVALID_REQUEST_STATUS);
        }

        request.setStatus(HouseRenterRequest.Status.APPROVED);
        request.setProcessedDate(LocalDateTime.now());
        houseRenterRequestRepository.save(request);

        User user = request.getUser();
        if (!houseRenterRepository.existsById(user.getId())) {
            HouseRenter newHouseRenter = new HouseRenter();
            newHouseRenter.setId(user.getId());
            newHouseRenter.setUser(user);
            newHouseRenter.setApprovedDate(LocalDateTime.now());
            houseRenterRepository.save(newHouseRenter);
        }

        return mapToDTO(request);
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO rejectRequest(Long id, String reason) {
        HouseRenterRequest request = findRequestByIdOrThrow(id);

        if (request.getStatus() != HouseRenterRequest.Status.PENDING) {
            throw new AppException(StatusCode.INVALID_REQUEST_STATUS);
        }

        request.setStatus(HouseRenterRequest.Status.REJECTED);
        request.setReason(reason);
        request.setProcessedDate(LocalDateTime.now());

        return mapToDTO(houseRenterRequestRepository.save(request));
    }

    private HouseRenterRequest findRequestByIdOrThrow(Long id) {
        return houseRenterRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.REQUEST_NOT_FOUND, id));
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
}