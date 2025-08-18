package com.codegym.service;

import com.codegym.dto.request.HouseRequest;
import com.codegym.dto.response.HouseDTO;
import java.util.List;
import java.util.Map;

public interface HouseService {

    List<HouseDTO> getAllHouses();

    HouseDTO getHouseById(Long id);

    HouseDTO createHouse(HouseRequest request);

    HouseDTO updateHouse(Long id, HouseRequest request);

    void deleteHouse(Long id);

    List<HouseDTO> searchHouses(String keyword);

    List<HouseDTO> getTopHouses();

    List<HouseDTO> getTopHousesByFavorites(int limit);

    HouseDTO updateHouseStatus(Long id, String status);

    List<String> getHouseImages(Long id);

    List<HouseDTO> getHousesByCurrentHost();

    // Admin methods
    org.springframework.data.domain.Page<HouseDTO> getAllHousesForAdmin(org.springframework.data.domain.Pageable pageable);

    // Dashboard statistics methods
    long countAllHouses();
    
    long countHousesByStatus(String status);
    
    List<Map<String, Object>> getRecentHousesForDashboard(int limit);

}