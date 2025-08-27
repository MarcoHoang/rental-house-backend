package com.codegym.service;

import com.codegym.dto.response.HostDTO;
import com.codegym.dto.response.HostDetailAdminDTO;
import com.codegym.dto.response.HouseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface HostService {

    Page<HostDTO> getAllHosts(Pageable pageable);

    // Thêm method tìm kiếm
    Page<HostDTO> searchHosts(String keyword, Boolean active, Pageable pageable);

    HostDTO getHostById(Long id);

    HostDTO createHost(HostDTO dto);

    HostDTO updateHost(Long id, HostDTO dto);

    void deleteHost(Long id);

    void lockHostByUserId(Long userId);
    void unlockHostByUserId(Long userId);

    List<HouseDTO> getHostHouses(Long id);

    Map<String, Object> getCurrentHostStats();

    HostDTO getCurrentHostDetails();

    HostDTO updateCurrentHostProfile(HostDTO dto);

    HostDetailAdminDTO getHostDetailsByUserId(Long userId);

    void changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword);

    // Admin method để update status của host
    HostDTO updateHostStatus(Long hostId, boolean active);

}

