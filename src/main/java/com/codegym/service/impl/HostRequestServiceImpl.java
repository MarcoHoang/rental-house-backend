package com.codegym.service.impl;

import com.codegym.dto.response.HostRequestDTO;
import com.codegym.entity.*;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HostRepository;
import com.codegym.repository.HostRequestRepository;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HostRequestService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostRequestServiceImpl implements HostRequestService {

    private final HostRequestRepository hostRequestRepository;
    private final HostRepository hostRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<HostRequestDTO> findAll(Pageable pageable) {
        return hostRequestRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HostRequestDTO> findPending(Pageable pageable) {
        return hostRequestRepository.findByStatus(HostRequest.Status.PENDING, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public HostRequestDTO findByUserId(Long userId) {
        return hostRequestRepository.findByUserId(userId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.REQUEST_NOT_FOUND, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public HostRequestDTO getCurrentUserRequest() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUserEmail));
        
        return hostRequestRepository.findByUserId(currentUser.getId())
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public HostRequestDTO createRequest(HostRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, dto.getUserId()));

        if (user.getRole().getName() == RoleName.HOST) {
            throw new AppException(StatusCode.USER_ALREADY_HOST, "Bạn đã là một chủ nhà.");
        }

        Optional<HostRequest> existingRequestOpt = hostRequestRepository.findByUserId(user.getId());

        HostRequest requestToSave;

        if (existingRequestOpt.isPresent()) {
            requestToSave = existingRequestOpt.get();

            if (requestToSave.getStatus() == HostRequest.Status.PENDING) {
                throw new AppException(StatusCode.PENDING_REQUEST_EXISTS, "Bạn đã có một đơn đăng ký đang chờ duyệt.");
            }

            requestToSave.setStatus(HostRequest.Status.PENDING);
            requestToSave.setRequestDate(LocalDateTime.now());
            requestToSave.setProcessedDate(null);
            requestToSave.setReason(null);

        } else {
            requestToSave = new HostRequest();
            requestToSave.setUser(user);
            requestToSave.setStatus(HostRequest.Status.PENDING);
        }
        requestToSave.setNationalId(dto.getNationalId());
        requestToSave.setProofOfOwnershipUrl(dto.getProofOfOwnershipUrl());
        requestToSave.setIdFrontPhotoUrl(dto.getIdFrontPhotoUrl());
        requestToSave.setIdBackPhotoUrl(dto.getIdBackPhotoUrl());
        
        // Cập nhật thông tin user nếu có thay đổi
        if (dto.getAddress() != null && !dto.getAddress().equals(user.getAddress())) {
            user.setAddress(dto.getAddress());
            userRepository.save(user);
        }
        if (dto.getPhone() != null && !dto.getPhone().equals(user.getPhone())) {
            user.setPhone(dto.getPhone());
            userRepository.save(user);
        }
        HostRequest savedEntity = hostRequestRepository.save(requestToSave);
        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional
    public HostRequestDTO approveRequest(Long id) {
        HostRequest request = findRequestByIdOrThrow(id);

        if (request.getStatus() != HostRequest.Status.PENDING) {
            throw new AppException(StatusCode.INVALID_REQUEST_STATUS);
        }

        request.setStatus(HostRequest.Status.APPROVED);
        request.setProcessedDate(LocalDateTime.now());
        hostRequestRepository.save(request);

        User user = request.getUser();
        
        Role hostRole = roleRepository.findByName(RoleName.HOST)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.ROLE_NOT_FOUND));
        user.setRole(hostRole);
        userRepository.save(user);

        if (!hostRepository.existsById(user.getId())) {
            Host newHost = new Host();
            newHost.setId(user.getId());
            newHost.setUser(user);
            newHost.setApprovedDate(LocalDateTime.now());

            newHost.setNationalId(request.getNationalId());
            newHost.setAddress(user.getAddress());
            newHost.setProofOfOwnershipUrl(request.getProofOfOwnershipUrl());

            hostRepository.save(newHost);
        }

        return mapToDTO(request);
    }

    @Override
    @Transactional
    public HostRequestDTO rejectRequest(Long id, String reason) {
        HostRequest request = findRequestByIdOrThrow(id);

        if (request.getStatus() != HostRequest.Status.PENDING) {
            throw new AppException(StatusCode.INVALID_REQUEST_STATUS);
        }

        request.setStatus(HostRequest.Status.REJECTED);
        request.setReason(reason);
        request.setProcessedDate(LocalDateTime.now());

        return mapToDTO(hostRequestRepository.save(request));
    }

    private HostRequest findRequestByIdOrThrow(Long id) {
        return hostRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.REQUEST_NOT_FOUND, id));
    }

    private HostRequestDTO mapToDTO(HostRequest entity) {
        User user = entity.getUser();

        return HostRequestDTO.builder()
                .id(entity.getId())
                .userId(user.getId())
                .userEmail(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .status(entity.getStatus().name())
                .reason(entity.getReason())
                .requestDate(entity.getRequestDate())
                .processedDate(entity.getProcessedDate())
                .nationalId(entity.getNationalId())
                .proofOfOwnershipUrl(entity.getProofOfOwnershipUrl())
                .idFrontPhotoUrl(entity.getIdFrontPhotoUrl())
                .idBackPhotoUrl(entity.getIdBackPhotoUrl())
                .address(user.getAddress())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HostRequestDTO findById(Long id) {
        return hostRequestRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.REQUEST_NOT_FOUND, id));
    }
}


