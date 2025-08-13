package com.codegym.service;

import com.codegym.dto.request.HouseRequest;
import com.codegym.dto.response.HouseDTO;
import java.util.List;

public interface HouseService {

    List<HouseDTO> getAllHouses();

    HouseDTO getHouseById(Long id);

    HouseDTO createHouse(HouseRequest request);

    HouseDTO updateHouse(Long id, HouseRequest request);

    void deleteHouse(Long id);

    List<HouseDTO> searchHouses(String keyword);

    List<HouseDTO> getTopHouses();

    HouseDTO updateHouseStatus(Long id, String status);

    List<String> getHouseImages(Long id);

    List<HouseDTO> getHousesByCurrentHost();

}