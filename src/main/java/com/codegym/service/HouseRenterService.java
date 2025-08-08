package com.codegym.service;

import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.HouseRenterDTO;
import java.util.List;

public interface HouseRenterService {

    List<HouseRenterDTO> getAllHouseRenters();

    HouseRenterDTO getHouseRenterById(Long id);

    HouseRenterDTO createHouseRenter(HouseRenterDTO dto);

    HouseRenterDTO updateHouseRenter(Long id, HouseRenterDTO dto);

    void deleteHouseRenter(Long id);

    void lockHouseRenter(Long id);

    void unlockHouseRenter(Long id);

    List<HouseDTO> getHouseRenterHouses(Long id);
}