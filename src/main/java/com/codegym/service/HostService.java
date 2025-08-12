package com.codegym.service;

import com.codegym.dto.response.HostDTO;
import com.codegym.dto.response.HouseDTO;
import java.util.List;

public interface HostService {

    List<HostDTO> getAllHosts();

    HostDTO getHostById(Long id);

    HostDTO createHost(HostDTO dto);

    HostDTO updateHost(Long id, HostDTO dto);

    void deleteHost(Long id);

    void lockHost(Long id);

    void unlockHost(Long id);

    List<HouseDTO> getHostHouses(Long id);
}
