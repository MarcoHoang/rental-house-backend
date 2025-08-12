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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<HostRequestDTO> findAll() {
        return hostRequestRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
                .orElse(null); // Return null if no request exists
    }

    @Override
    @Transactional
    public HostRequestDTO createRequest(HostRequestDTO dto) {
        // Validate user exists and is active
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, dto.getUserId()));

        if (!user.isActive()) {
            throw new AppException(StatusCode.ACCOUNT_LOCKED);
        }

        // Check if user is already a host
        if (hostRepository.existsById(dto.getUserId())) {
            throw new AppException(StatusCode.USER_ALREADY_HOST);
        }

        // Check if there's already a pending request
        if (hostRequestRepository.existsByUserIdAndStatus(dto.getUserId(), HostRequest.Status.PENDING)) {
            throw new AppException(StatusCode.PENDING_REQUEST_EXISTS);
        }

        HostRequest entity = new HostRequest();
        entity.setUser(user);
        entity.setRequestDate(LocalDateTime.now());
        entity.setStatus(HostRequest.Status.PENDING);

        HostRequest savedEntity = hostRequestRepository.save(entity);
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
        
        // Update user role to HOST
        Role hostRole = roleRepository.findByName(RoleName.HOST)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.ROLE_NOT_FOUND));
        user.setRole(hostRole);
        userRepository.save(user);

        // Create Host record if not exists
        if (!hostRepository.existsById(user.getId())) {
            Host newHost = new Host();
            newHost.setId(user.getId());
            newHost.setUser(user);
            newHost.setApprovedDate(LocalDateTime.now());
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
        return HostRequestDTO.builder()
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
