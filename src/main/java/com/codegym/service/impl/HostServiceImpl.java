package com.codegym.service.impl;

import com.codegym.dto.response.HostDTO;
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
import com.codegym.repository.UserRepository;
import com.codegym.service.HostService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    private Host findHostByIdOrThrow(Long id) {
        return hostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, id));
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, id));
    }

    private HostDTO toDTO(Host host) {
        HostDTO dto = new HostDTO();
        dto.setId(host.getId());
        dto.setNationalId(host.getNationalId());
        dto.setProofOfOwnershipUrl(host.getProofOfOwnershipUrl());
        dto.setAddress(host.getAddress());
        dto.setApprovedDate(host.getApprovedDate());
        dto.setApproved(host.getApprovedDate() != null);

        if (host.getUser() != null) {
            User user = host.getUser();
            dto.setFullName(user.getFullName());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAvatar(user.getAvatarUrl());
        }

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
                .title(house.getTitle())
                .address(house.getAddress())
                .price(house.getPrice())
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
    public HostDTO getHostById(Long id) {
        Host host = findHostByIdOrThrow(id);
        return toDTO(host);
    }

    @Override
    @Transactional
    public HostDTO createHost(HostDTO dto) {
        User user = findUserByIdOrThrow(dto.getId());

        if (hostRepository.existsById(dto.getId())) {
            throw new AppException(StatusCode.USER_ALREADY_HOST);
        }

        Host host = new Host();
        host.setId(user.getId());
        updateEntityFromDTO(host, dto, user);

        Host savedHost = hostRepository.save(host);
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
    public void lockHost(Long id) {
        User user = findUserByIdOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlockHost(Long id) {
        User user = findUserByIdOrThrow(id);
        user.setActive(true);
        userRepository.save(user);
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
}

