package com.codegym.service.impl;

import com.codegym.dto.response.HouseDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    private House findHouseByIdOrThrow(Long id) {
        return houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, id));
    }

    private User findHouseRenterByIdOrThrow(Long id) {
        // Assuming a house renter is a user, we check for user existence
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_RENTER_NOT_FOUND, id));
    }

    private HouseDTO toDTO(House house) {
        return HouseDTO.builder()
                .id(house.getId())
                .houseRenterId(house.getHouseRenter().getId())
                .houseRenterName(house.getHouseRenter().getUsername())
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
                        : List.of())
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .build();
    }

    private void updateEntityFromDTO(House house, HouseDTO dto, User houseRenter) {
        house.setHouseRenter(houseRenter);
        house.setTitle(dto.getTitle());
        house.setDescription(dto.getDescription());
        house.setAddress(dto.getAddress());
        house.setPrice(dto.getPrice());
        house.setArea(dto.getArea());
        house.setLatitude(dto.getLatitude());
        house.setLongitude(dto.getLongitude());
        house.setStatus(dto.getStatus());
        house.setHouseType(dto.getHouseType());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseDTO getHouseById(Long id) {
        House house = findHouseByIdOrThrow(id);
        return toDTO(house);
    }

    @Override
    @Transactional
    public HouseDTO createHouse(HouseDTO dto) {
        User houseRenter = findHouseRenterByIdOrThrow(dto.getHouseRenterId());

        House house = new House();
        updateEntityFromDTO(house, dto, houseRenter);
        house.setId(null);

        House savedHouse = houseRepository.save(house);
        return toDTO(savedHouse);
    }

    @Override
    @Transactional
    public HouseDTO updateHouse(Long id, HouseDTO dto) {
        House existingHouse = findHouseByIdOrThrow(id);
        User houseRenter = findHouseRenterByIdOrThrow(dto.getHouseRenterId());

        updateEntityFromDTO(existingHouse, dto, houseRenter);

        House updatedHouse = houseRepository.save(existingHouse);
        return toDTO(updatedHouse);
    }

    @Override
    @Transactional
    public void deleteHouse(Long id) {
        House house = findHouseByIdOrThrow(id);
        houseRepository.delete(house);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> searchHouses(String keyword) {
        return houseRepository.findAll().stream()
                .filter(h -> keyword == null || h.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getTopHouses() {
        return houseRepository.findAll().stream().limit(5).map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HouseDTO updateHouseStatus(Long id, String status) {
        House house = findHouseByIdOrThrow(id);

        boolean isValidStatus = Arrays.stream(House.Status.values())
                .anyMatch(s -> s.name().equalsIgnoreCase(status));

        if (!isValidStatus) {
            throw new AppException(StatusCode.INVALID_HOUSE_STATUS, status);
        }

        house.setStatus(House.Status.valueOf(status.toUpperCase()));
        return toDTO(houseRepository.save(house));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getHouseImages(Long id) {
        House house = findHouseByIdOrThrow(id);
        return house.getImages() != null
                ? house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                : List.of();
    }
}