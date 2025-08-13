package com.codegym.service;

import com.codegym.dto.response.HouseDTO;
import java.util.List;

public interface HouseService {

    List<HouseDTO> getAllHouses();

    HouseDTO getHouseById(Long id);

    HouseDTO createHouse(HouseDTO dto);

    HouseDTO updateHouse(Long id, HouseDTO dto);

    void deleteHouse(Long id);

    List<HouseDTO> searchHouses(String keyword);

    List<HouseDTO> getTopHouses();

    HouseDTO updateHouseStatus(Long id, String status);

    List<String> getHouseImages(Long id);

    List<HouseDTO> getHousesByCurrentHost();

}