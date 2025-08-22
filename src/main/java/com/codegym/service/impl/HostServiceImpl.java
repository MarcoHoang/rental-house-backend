package com.codegym.service.impl;

import com.codegym.dto.response.HostDTO;
import com.codegym.dto.response.HostDetailAdminDTO;
import com.codegym.dto.response.HouseDTO;
import com.codegym.entity.Host;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.User;
import com.codegym.entity.RoleName;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HostRepository;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HostService;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final RentalRepository rentalRepository;
    private final UserService userService;

    private Host findHostByUserIdOrThrow(Long userId) {
        Host host = hostRepository.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, userId));
        
        // Đảm bảo user data được load
        if (host.getUser() == null) {
            log.warn("Host found but user data is null for userId: {}", userId);
            throw new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, userId);
        }
        
        return host;
    }

    private User getCurrentAuthenticatedUser() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUserEmail));
    }

    private Host findHostByIdOrThrow(Long id) {
        Host host = hostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, id));
        
        // Đảm bảo user data được load
        if (host.getUser() == null) {
            log.warn("Host found but user data is null for hostId: {}", id);
            throw new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, id);
        }
        
        return host;
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, id));
    }

    private HostDTO toDTO(Host host) {
        User user = host.getUser();
        if (user == null) {
            return null;
        }

        HostDTO dto = new HostDTO();

        // Set hostId và userId
        dto.setId(host.getId()); // hostId
        dto.setUserId(user.getId()); // userId

        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatarUrl());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setActive(user.isActive());

        dto.setNationalId(host.getNationalId());
        dto.setProofOfOwnershipUrl(host.getProofOfOwnershipUrl());
        dto.setAddress(host.getAddress());
        dto.setApprovedDate(host.getApprovedDate());
        dto.setApproved(host.getApprovedDate() != null);

        return dto;
    }

    private void updateEntityFromDTO(Host host, HostDTO dto, User user) {
        host.setUser(user);
        host.setNationalId(dto.getNationalId());
        host.setProofOfOwnershipUrl(dto.getProofOfOwnershipUrl());
        host.setAddress(dto.getAddress());
        host.setApprovedDate(dto.getApprovedDate());
    }

    private HouseDTO toHouseDTO(House house) {
        return HouseDTO.builder()
                .id(house.getId())
                .hostId(house.getHost().getId())
                .hostName(house.getHost().getUsername())
                .hostAvatar(house.getHost().getAvatarUrl())
                .title(house.getTitle())
                .address(house.getAddress())
                .price(house.getPrice())
                .status(house.getStatus())
                .houseType(house.getHouseType())
                .imageUrls(house.getImages() != null ?
                        house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HostDTO> getAllHosts(Pageable pageable) {
        return hostRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HostDTO> searchHosts(String keyword, Boolean active, Pageable pageable) {
        Page<Host> hosts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            hosts = hostRepository.findByKeywordAndActive(keyword.trim(), active, pageable);
        } else {
            hosts = hostRepository.findByActiveOnly(active, pageable);
        }
        return hosts.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public HostDTO getHostById(Long id) {
        Host host = findHostByIdOrThrow(id);
        return toDTO(host);
    }

    @Override
    @Transactional
    public HostDTO createHost(HostDTO dto) {
        // Khi tạo host, dto.getUserId() sẽ là userId của user muốn trở thành host
        User user = findUserByIdOrThrow(dto.getUserId());

        // Kiểm tra xem user đã là host chưa
        if (hostRepository.findByUser(user.getId()).isPresent()) {
            throw new AppException(StatusCode.USER_ALREADY_HOST);
        }

        Host host = new Host();
        host.setUser(user);
        updateEntityFromDTO(host, dto, user);

        Host savedHost = hostRepository.save(host);
        log.info("Created host with hostId: {} for user: {} (email: {})", 
                savedHost.getId(), user.getId(), user.getEmail());
        return toDTO(savedHost);
    }

    @Override
    @Transactional
    public HostDTO updateHost(Long id, HostDTO dto) {
        Host existingHost = findHostByIdOrThrow(id);
        updateEntityFromDTO(existingHost, dto, existingHost.getUser());
        Host updatedHost = hostRepository.save(existingHost);
        return toDTO(updatedHost);
    }

    @Override
    @Transactional
    public void deleteHost(Long id) {
        Host host = findHostByIdOrThrow(id);
        hostRepository.delete(host);
    }

    @Override
    @Transactional
    public void lockHostByUserId(Long userId) {
        log.info("Locking host with userId: {}", userId);
        Host host = findHostByUserIdOrThrow(userId);
        User userToUpdate = host.getUser();
        log.info("Found user: {} (email: {}), current active status: {}", 
                userToUpdate.getId(), userToUpdate.getEmail(), userToUpdate.isActive());
        userToUpdate.setActive(false);
        User savedUser = userRepository.save(userToUpdate);
        log.info("User locked successfully. New active status: {}", savedUser.isActive());
    }

    @Override
    @Transactional
    public void unlockHostByUserId(Long userId) {
        log.info("Unlocking host with userId: {}", userId);
        Host host = findHostByUserIdOrThrow(userId);
        User userToUpdate = host.getUser();
        log.info("Found user: {} (email: {}), current active status: {}", 
                userToUpdate.getId(), userToUpdate.getEmail(), userToUpdate.isActive());
        userToUpdate.setActive(true);
        User savedUser = userRepository.save(userToUpdate);
        log.info("User unlocked successfully. New active status: {}", savedUser.isActive());
    }


    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getHostHouses(Long id) {
        Host host = findHostByIdOrThrow(id);
        List<House> houses = houseRepository.findByHostId(host.getId());
        return houses.stream().map(this::toHouseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCurrentHostStats() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

        if (!currentUser.getRole().getName().equals(RoleName.HOST)) {
            throw new AppException(StatusCode.UNAUTHORIZED, "Người dùng không phải là chủ nhà");
        }

        List<House> houses = houseRepository.findByHost(currentUser);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalHouses", houses.size());
        stats.put("activeHouses", houses.stream().filter(h -> "ACTIVE".equals(h.getStatus().name())).count());
        stats.put("inactiveHouses", houses.stream().filter(h -> "INACTIVE".equals(h.getStatus().name())).count());
        stats.put("totalRevenue", houses.stream().mapToDouble(House::getPrice).sum());

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public HostDTO getCurrentHostDetails() {
        User currentUser = getCurrentAuthenticatedUser();
        Host host = hostRepository.findByUser(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, currentUser.getId()));

        return toDTO(host);
    }

    @Override
    @Transactional
    public HostDTO updateCurrentHostProfile(HostDTO dto) {
        User currentUser = getCurrentAuthenticatedUser();

        Host hostToUpdate = hostRepository.findByUser(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        StatusCode.HOST_NOT_FOUND, currentUser.getId()
                ));

        if (dto.getFullName() != null) {
            currentUser.setFullName(dto.getFullName().trim());
        }
        if (dto.getPhone() != null) {
            currentUser.setPhone(dto.getPhone().trim());
        }
        if (dto.getAvatarUrl() != null && !dto.getAvatarUrl().isBlank()) {
            currentUser.setAvatarUrl(dto.getAvatarUrl().trim());
        }
        userRepository.save(currentUser);

        if (dto.getAddress() != null) {
            hostToUpdate.setAddress(dto.getAddress().trim());
        }
        if (dto.getNationalId() != null) {
            hostToUpdate.setNationalId(dto.getNationalId().trim());
        }
        hostRepository.save(hostToUpdate);

        log.info("Updated host profile - hostId: {}, userId: {}, email: {}", 
                hostToUpdate.getId(), currentUser.getId(), currentUser.getEmail());

        return toDTO(hostToUpdate);
    }


    @Override
    @Transactional(readOnly = true)
    public HostDetailAdminDTO getHostDetailsByUserId(Long userId) {
        User user = findUserByIdOrThrow(userId);
        Host host = hostRepository.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, userId));

        List<House> houseEntities = houseRepository.findByHost(user);
        List<HouseDTO> houseDTOs = houseEntities.stream()
                .map(this::toHouseDTO).collect(Collectors.toList());

        Double revenue = rentalRepository.sumTotalPriceByHost(user.getId());
        BigDecimal totalRevenue = (revenue != null) ? BigDecimal.valueOf(revenue) : BigDecimal.ZERO;

        return HostDetailAdminDTO.builder()
                .id(user.getId())
                .avatarUrl(user.getAvatarUrl())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .active(user.isActive())
                .address(host.getAddress())
                .nationalId(host.getNationalId())
                .totalRevenue(totalRevenue)
                .houses(houseDTOs)
                .build();
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword) {
        // Kiểm tra xem user có phải là host không
        User currentUser = getCurrentAuthenticatedUser();
        if (!currentUser.getId().equals(userId)) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION);
        }

        // Gọi UserService để thực hiện đổi mật khẩu
        userService.changePassword(userId, oldPassword, newPassword, confirmPassword);
    }

    @Override
    @Transactional
    public HostDTO updateHostStatus(Long hostId, boolean active) {
        log.info("Updating host status - hostId: {}, active: {}", hostId, active);
        
        // Tìm host theo hostId (id của bảng hosts)
        Host host = findHostByIdOrThrow(hostId);
        User user = host.getUser();
        
        log.info("Found host: {} with user: {} (email: {}), current active status: {}", 
                hostId, user.getId(), user.getEmail(), user.isActive());
        
        // Cập nhật trạng thái active của user
        user.setActive(active);
        User savedUser = userRepository.save(user);
        
        log.info("User status updated successfully. New active status: {}", savedUser.isActive());
        
        // Trả về HostDTO đã cập nhật
        return toDTO(host);
    }
}

