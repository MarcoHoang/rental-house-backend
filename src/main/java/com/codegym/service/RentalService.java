package com.codegym.service;

import com.codegym.dto.response.RentalDTO;

import java.util.List;

public interface RentalService {
    List<RentalDTO> findAll();
    RentalDTO findById(Long id);
    RentalDTO create(RentalDTO rentalDTO);
    RentalDTO update(Long id, RentalDTO rentalDTO);
    void delete(Long id);

    // Thêm các phương thức mới
    List<RentalDTO> getUserRentals(Long userId);
    List<RentalDTO> getHouseRenterRentals(Long landlordId);
    List<RentalDTO> searchRentals(String keyword);
    RentalDTO checkin(Long id);
    RentalDTO checkout(Long id);
    Double getHouseRenterIncome(Long landlordId);
}
