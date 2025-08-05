package com.codegym.service.impl;

import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.entity.HouseRenter;
import com.codegym.entity.User;
import com.codegym.repository.HouseRenterRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseRenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseRenterServiceImpl implements HouseRenterService {

    @Autowired
    private HouseRenterRepository houseRenterRepository;

    @Autowired
    private UserRepository userRepository;

    private HouseRenterDTO toDTO(HouseRenter houseRenter) {
        HouseRenterDTO dto = new HouseRenterDTO();
        dto.setId(houseRenter.getId());
        dto.setNationalId(houseRenter.getNationalId());
        dto.setProofOfOwnershipUrl(houseRenter.getProofOfOwnershipUrl());
        dto.setAddress(houseRenter.getAddress());
        dto.setApprovedDate(houseRenter.getApprovedDate());
        dto.setApproved(houseRenter.getApprovedDate() != null); // approved = true nếu approvedDate != null

        if (houseRenter.getUser() != null) {
            dto.setFullName(houseRenter.getUser().getFullName());
            dto.setUsername(houseRenter.getUser().getUsername());
            dto.setEmail(houseRenter.getUser().getEmail());
            dto.setPhone(houseRenter.getUser().getPhone());
            dto.setAvatar(houseRenter.getUser().getAvatarUrl());
        }

        return dto;
    }

    private HouseRenter toEntity(HouseRenterDTO dto, User user) {
        HouseRenter houseRenter = new HouseRenter();
        houseRenter.setId(dto.getId());
        houseRenter.setUser(user);
        houseRenter.setNationalId(dto.getNationalId());
        houseRenter.setProofOfOwnershipUrl(dto.getProofOfOwnershipUrl());
        houseRenter.setAddress(dto.getAddress());
        houseRenter.setApprovedDate(dto.getApprovedDate());

        return houseRenter;
    }

    @Override
    public List<HouseRenterDTO> getAllLandlords() {
        return houseRenterRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public HouseRenterDTO getLandlordById(Long id) {
        return houseRenterRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public HouseRenterDTO createLandlord(HouseRenterDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        HouseRenter houseRenter = toEntity(dto, user);
        houseRenter.setId(user.getId());
        return toDTO(houseRenterRepository.save(houseRenter));
    }

    @Override
    public HouseRenterDTO updateLandlord(Long id, HouseRenterDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        HouseRenter houseRenter = toEntity(dto, user);
        houseRenter.setId(id);
        return toDTO(houseRenterRepository.save(houseRenter));
    }

    @Override
    public void deleteLandlord(Long id) {
        houseRenterRepository.deleteById(id);
    }
}
