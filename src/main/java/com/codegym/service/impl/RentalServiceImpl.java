
package com.codegym.service.impl;

import com.codegym.dto.response.RentalDTO;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<RentalDTO> findAll() {
        return rentalRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RentalDTO findById(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow();
        return convertToDTO(rental);
    }

    @Override
    public RentalDTO create(RentalDTO dto) {
        Rental rental = convertToEntity(dto);
        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    public RentalDTO update(Long id, RentalDTO dto) {
        Rental rental = rentalRepository.findById(id).orElseThrow();
        rental.setStartDate(dto.getStartDate());
        rental.setEndDate(dto.getEndDate());
        rental.setStatus(dto.getStatus());
        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    public void delete(Long id) {
        rentalRepository.deleteById(id);
    }

    private RentalDTO convertToDTO(Rental rental) {
        return RentalDTO.builder()
                .id(rental.getId())
                .houseId(rental.getHouse().getId())
                .renterId(rental.getRenter().getId())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .status(rental.getStatus())
                .createdAt(rental.getCreatedAt())
                .updatedAt(rental.getUpdatedAt())
                .build();
    }

    private Rental convertToEntity(RentalDTO dto) {
        House house = houseRepository.findById(dto.getHouseId()).orElseThrow();
        User renter = userRepository.findById(dto.getRenterId()).orElseThrow();

        return Rental.builder()
                .house(house)
                .renter(renter)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .build();
    }

    @Override
    public List<RentalDTO> getUserRentals(Long userId) {
        // TODO: Lấy lịch sử thuê nhà của user
        return rentalRepository.findAll().stream().filter(r -> r.getRenter().getId().equals(userId)).map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getHouseRenterRentals(Long houseRenterId) {
        // TODO: Lấy lịch thuê nhà của chủ nhà
        return rentalRepository.findAll().stream().filter(r -> r.getHouse().getHouseRenter().getId().equals(houseRenterId)).map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> searchRentals(String keyword) {
        // TODO: Tìm kiếm lịch thuê theo keyword
        return rentalRepository.findAll().stream().filter(r -> keyword == null || r.getHouse().getTitle().toLowerCase().contains(keyword.toLowerCase())).map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public RentalDTO checkin(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow();
        rental.setStatus(com.codegym.entity.Rental.Status.CHECKED_IN);
        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    public RentalDTO checkout(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow();
        rental.setStatus(com.codegym.entity.Rental.Status.CHECKED_OUT);
        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    public Double getHouseRenterIncome(Long houseRenterId) {
        // TODO: Thống kê thu nhập theo tháng của chủ nhà
        return rentalRepository.findAll().stream().filter(r -> r.getHouse().getHouseRenter().getId().equals(houseRenterId)).mapToDouble(r -> r.getTotalPrice() != null ? r.getTotalPrice() : 0).sum();
    }
}
