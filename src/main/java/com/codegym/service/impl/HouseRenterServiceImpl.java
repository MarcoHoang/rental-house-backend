package com.codegym.service.impl;

import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.HouseRenter;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.HouseRenterRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseRenterService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseRenterServiceImpl implements HouseRenterService {

    private final HouseRenterRepository houseRenterRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    private HouseRenter findHouseRenterByIdOrThrow(Long id) {
        return houseRenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_RENTER_NOT_FOUND, id));
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, id));
    }

    private HouseRenterDTO toDTO(HouseRenter houseRenter) {
        HouseRenterDTO dto = new HouseRenterDTO();
        dto.setId(houseRenter.getId());
        dto.setNationalId(houseRenter.getNationalId());
        dto.setProofOfOwnershipUrl(houseRenter.getProofOfOwnershipUrl());
        dto.setAddress(houseRenter.getAddress());
        dto.setApprovedDate(houseRenter.getApprovedDate());
        dto.setApproved(houseRenter.getApprovedDate() != null);

        if (houseRenter.getUser() != null) {
            User user = houseRenter.getUser();
            dto.setFullName(user.getFullName());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAvatar(user.getAvatarUrl());
        }

        return dto;
    }

    private void updateEntityFromDTO(HouseRenter houseRenter, HouseRenterDTO dto, User user) {
        houseRenter.setUser(user);
        houseRenter.setNationalId(dto.getNationalId());
        houseRenter.setProofOfOwnershipUrl(dto.getProofOfOwnershipUrl());
        houseRenter.setAddress(dto.getAddress());
        houseRenter.setApprovedDate(dto.getApprovedDate());
    }

    private HouseDTO toHouseDTO(House house) {
        return HouseDTO.builder()
                .id(house.getId())
                .houseRenterId(house.getHouseRenter().getId())
                .houseRenterName(house.getHouseRenter().getUsername())
                .title(house.getTitle())
                .address(house.getAddress())
                .price(house.getPrice())
                .imageUrls(house.getImages() != null ?
                        house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseRenterDTO> getAllHouseRenters() {
        return houseRenterRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseRenterDTO getHouseRenterById(Long id) {
        HouseRenter houseRenter = findHouseRenterByIdOrThrow(id);
        return toDTO(houseRenter);
    }

    @Override
    @Transactional
    public HouseRenterDTO createHouseRenter(HouseRenterDTO dto) {
        User user = findUserByIdOrThrow(dto.getId());

        if (houseRenterRepository.existsById(dto.getId())) {
            throw new AppException(StatusCode.USER_ALREADY_HOUSE_RENTER);
        }

        HouseRenter houseRenter = new HouseRenter();
        houseRenter.setId(user.getId());
        updateEntityFromDTO(houseRenter, dto, user);

        HouseRenter savedHouseRenter = houseRenterRepository.save(houseRenter);
        return toDTO(savedHouseRenter);
    }

    @Override
    @Transactional
    public HouseRenterDTO updateHouseRenter(Long id, HouseRenterDTO dto) {
        HouseRenter existingHouseRenter = findHouseRenterByIdOrThrow(id);
        updateEntityFromDTO(existingHouseRenter, dto, existingHouseRenter.getUser());
        HouseRenter updatedHouseRenter = houseRenterRepository.save(existingHouseRenter);
        return toDTO(updatedHouseRenter);
    }

    @Override
    @Transactional
    public void deleteHouseRenter(Long id) {
        HouseRenter houseRenter = findHouseRenterByIdOrThrow(id);
        houseRenterRepository.delete(houseRenter);
    }

    @Override
    @Transactional
    public void lockHouseRenter(Long id) {
        User user = findUserByIdOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlockHouseRenter(Long id) {
        User user = findUserByIdOrThrow(id);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getHouseRenterHouses(Long id) {
        HouseRenter houseRenter = findHouseRenterByIdOrThrow(id);
        List<House> houses = houseRepository.findByHouseRenterId(houseRenter.getId());
        return houses.stream().map(this::toHouseDTO).collect(Collectors.toList());
    }
}