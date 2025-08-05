package com.codegym.service.impl;

import com.codegym.dto.response.HouseDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    private HouseDTO toDTO(House house) {
        return HouseDTO.builder()
                .id(house.getId())
                .landlordId(house.getHouseRenter().getId())
                .landlordName(house.getHouseRenter().getUsername())
                .title(house.getTitle())
                .description(house.getDescription())
                .address(house.getAddress())
                .price(house.getPrice())
                .area(house.getArea())
                .latitude(house.getLatitude())
                .longitude(house.getLongitude())
                .status(house.getStatus())
                .houseType(house.getHouseType())
                .imageUrls(house.getImages() != null
                        ? house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                        : null)
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .build();
    }

    private House toEntity(HouseDTO dto, User landlord) {
        House house = new House();
        house.setId(dto.getId());
        house.setHouseRenter(landlord);
        house.setTitle(dto.getTitle());
        house.setDescription(dto.getDescription());
        house.setAddress(dto.getAddress());
        house.setPrice(dto.getPrice());
        house.setArea(dto.getArea());
        house.setLatitude(dto.getLatitude());
        house.setLongitude(dto.getLongitude());
        house.setStatus(dto.getStatus());
        house.setHouseType(dto.getHouseType());
        return house;
    }

    @Override
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public HouseDTO getHouseById(Long id) {
        return houseRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public HouseDTO createHouse(HouseDTO dto) {
        User landlord = userRepository.findById(dto.getLandlordId())
                .orElseThrow(() -> new RuntimeException("Landlord not found"));
        House house = toEntity(dto, landlord);
        house.setId(null); // tạo mới
        return toDTO(houseRepository.save(house));
    }

    @Override
    public HouseDTO updateHouse(Long id, HouseDTO dto) {
        User landlord = userRepository.findById(dto.getLandlordId())
                .orElseThrow(() -> new RuntimeException("Landlord not found"));
        House house = toEntity(dto, landlord);
        house.setId(id); // cập nhật
        return toDTO(houseRepository.save(house));
    }

    @Override
    public void deleteHouse(Long id) {
        houseRepository.deleteById(id);
    }
}
