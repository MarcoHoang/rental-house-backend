package com.codegym.service.impl;

import com.codegym.dto.response.HouseRenterRequestDTO;
import com.codegym.entity.HouseRenter;
import com.codegym.entity.HouseRenterRequest;
import com.codegym.entity.User;
import com.codegym.repository.HouseRenterRepository;
import com.codegym.repository.HouseRenterRequestRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseRenterRequestService;
import jakarta.persistence.EntityNotFoundException;
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
    private final HouseRenterRepository houseRenterRepository; // Cần để tạo chủ nhà mới
    private final UserRepository userRepository;             // Cần để tìm người dùng

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
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu từ người dùng có ID: " + userId));
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO createRequest(HouseRenterRequestDTO dto) {
        Long userId = dto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userId + " để tạo yêu cầu."));

        // ----- KIỂM TRA LOGIC NGHIỆP VỤ -----
        if (houseRenterRepository.existsById(userId)) {
            throw new IllegalArgumentException("Người dùng này đã là một chủ nhà.");
        }
        if (houseRenterRequestRepository.existsByUserIdAndStatus(userId, HouseRenterRequest.Status.PENDING)) {
            throw new IllegalArgumentException("Bạn đã có một yêu cầu đang chờ xử lý. Vui lòng đợi quản trị viên phê duyệt.");
        }

        // ----- TẠO MỚI YÊU CẦU -----
        HouseRenterRequest entity = new HouseRenterRequest();
        entity.setUser(user);
        entity.setRequestDate(LocalDateTime.now());
        entity.setStatus(HouseRenterRequest.Status.PENDING);
        // Map các thông tin khác từ DTO nếu có, ví dụ:
        // entity.setNationalId(dto.getNationalId());
        // entity.setProofOfOwnershipUrl(dto.getProofOfOwnershipUrl());

        HouseRenterRequest savedEntity = houseRenterRequestRepository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO approveRequest(Long id) {
        // 1. Lấy yêu cầu từ DB
        HouseRenterRequest request = houseRenterRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu với ID: " + id));

        // 2. Kiểm tra trạng thái yêu cầu
        if (request.getStatus() != HouseRenterRequest.Status.PENDING) {
            throw new IllegalStateException("Chỉ có thể duyệt các yêu cầu đang ở trạng thái PENDING.");
        }

        // 3. Cập nhật trạng thái yêu cầu
        request.setStatus(HouseRenterRequest.Status.APPROVED);
        request.setProcessedDate(LocalDateTime.now());
        houseRenterRequestRepository.save(request);

        // 4. Logic cốt lõi: Tạo mới một bản ghi HouseRenter
        User user = request.getUser();
        if (!houseRenterRepository.existsById(user.getId())) {
            HouseRenter newHouseRenter = new HouseRenter();
            newHouseRenter.setId(user.getId());
            newHouseRenter.setUser(user);
            newHouseRenter.setApprovedDate(LocalDateTime.now());
            // Lấy thêm thông tin từ request nếu cần
            // newHouseRenter.setNationalId(request.getNationalId());
            houseRenterRepository.save(newHouseRenter);
        }

        // 5. (Tùy chọn) Cập nhật Role cho User nếu hệ thống của bạn có phân quyền
        // Role landlordRole = roleRepository.findByName("ROLE_LANDLORD").orElseThrow(...);
        // user.getRoles().add(landlordRole);
        // userRepository.save(user);

        return mapToDTO(request);
    }

    @Override
    @Transactional
    public HouseRenterRequestDTO rejectRequest(Long id, String reason) {
        HouseRenterRequest request = houseRenterRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu với ID: " + id));

        if (request.getStatus() != HouseRenterRequest.Status.PENDING) {
            throw new IllegalStateException("Chỉ có thể từ chối các yêu cầu đang ở trạng thái PENDING.");
        }

        request.setStatus(HouseRenterRequest.Status.REJECTED);
        request.setReason(reason);
        request.setProcessedDate(LocalDateTime.now());

        return mapToDTO(houseRenterRequestRepository.save(request));
    }

    // Phương thức mapper giữ nguyên
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