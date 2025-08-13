package com.codegym.service;

import com.codegym.dto.response.HostDTO;
import com.codegym.dto.response.HouseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface HostService {

    Page<HostDTO> getAllHosts(Pageable pageable);


    HostDTO getHostById(Long id);

    HostDTO createHost(HostDTO dto);

    HostDTO updateHost(Long id, HostDTO dto);

    void deleteHost(Long id);

    void lockHost(Long id);

    void unlockHost(Long id);

    List<HouseDTO> getHostHouses(Long id);

    Map<String, Object> getCurrentHostStats();

    HostDTO getCurrentHostDetails();

    HostDTO updateCurrentHostProfile(HostDTO dto);

}

