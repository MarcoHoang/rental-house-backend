package com.codegym.service;

import com.codegym.dto.response.LandlordDTO;

import java.util.List;

public interface LandlordService {
    List<LandlordDTO> getAllLandlords();
    LandlordDTO getLandlordById(Long id);
    LandlordDTO createLandlord(LandlordDTO dto);
    LandlordDTO updateLandlord(Long id, LandlordDTO dto);
    void deleteLandlord(Long id);
}
