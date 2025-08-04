package com.codegym.service;

import com.codegym.dto.response.RentalDTO;

import java.util.List;

public interface RentalService {
    List<RentalDTO> findAll();
    RentalDTO findById(Long id);
    RentalDTO create(RentalDTO rentalDTO);
    RentalDTO update(Long id, RentalDTO rentalDTO);
    void delete(Long id);
}
