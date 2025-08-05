package com.codegym.service.impl;

import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.entity.HouseRenter;
import com.codegym.entity.User;
import com.codegym.repository.HouseRenterRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseRenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        dto.setFullName(houseRenter.getFullName());
        dto.setAddress(houseRenter.getAddress());
        dto.setPhone(houseRenter.getPhone());
        dto.setAvatar(houseRenter.getAvatar());
        dto.setApproved(houseRenter.isApproved());
        dto.setUsername(houseRenter.getUser().getUsername());
        dto.setEmail(houseRenter.getUser().getEmail());
        return dto;
    }

    private HouseRenter toEntity(HouseRenterDTO dto, User user) {
        HouseRenter houseRenter = new HouseRenter();
        houseRenter.setId(dto.getId());
        houseRenter.setUser(user);
        houseRenter.setFullName(dto.getFullName());
        houseRenter.setAddress(dto.getAddress());
        houseRenter.setPhone(dto.getPhone());
        houseRenter.setAvatar(dto.getAvatar() != null ? dto.getAvatar() : "/images/default-avatar.png");
        houseRenter.setApproved(dto.isApproved());
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
