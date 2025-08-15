package com.codegym.service;

import com.codegym.dto.response.RentalDTO;
import java.util.List;
import java.util.Map;

public interface RentalService {

    List<RentalDTO> findAll();

    RentalDTO findById(Long id);

    RentalDTO create(RentalDTO dto);

    RentalDTO update(Long id, RentalDTO dto);

    void delete(Long id);

    RentalDTO checkin(Long id);

    RentalDTO checkout(Long id);

    List<RentalDTO> getUserRentals(Long userId);

    List<RentalDTO> getCurrentUserRentals();

    List<RentalDTO> getHostRentals(Long hostId);

    Map<String, Double> getHostIncome(Long hostId);
}