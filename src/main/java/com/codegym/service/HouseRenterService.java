package com.codegym.service;

import com.codegym.dto.response.HouseRenterDTO;

import java.util.List;

public interface HouseRenterService {
    List<HouseRenterDTO> getAllLandlords();
    HouseRenterDTO getLandlordById(Long id);
    HouseRenterDTO createLandlord(HouseRenterDTO dto);
    HouseRenterDTO updateLandlord(Long id, HouseRenterDTO dto);
    void deleteLandlord(Long id);
}
