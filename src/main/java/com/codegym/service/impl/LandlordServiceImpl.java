package com.codegym.service.impl;

import com.codegym.dto.response.LandlordDTO;
import com.codegym.entity.Landlord;
import com.codegym.entity.User;
import com.codegym.repository.LandlordRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.LandlordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LandlordServiceImpl implements LandlordService {

    @Autowired
    private LandlordRepository landlordRepository;

    @Autowired
    private UserRepository userRepository;

    private LandlordDTO toDTO(Landlord landlord) {
        LandlordDTO dto = new LandlordDTO();
        dto.setId(landlord.getId());
        dto.setFullName(landlord.getFullName());
        dto.setAddress(landlord.getAddress());
        dto.setPhone(landlord.getPhone());
        dto.setAvatar(landlord.getAvatar());
        dto.setApproved(landlord.isApproved());
        dto.setUsername(landlord.getUser().getUsername());
        dto.setEmail(landlord.getUser().getEmail());
        return dto;
    }

    private Landlord toEntity(LandlordDTO dto, User user) {
        Landlord landlord = new Landlord();
        landlord.setId(dto.getId());
        landlord.setUser(user);
        landlord.setFullName(dto.getFullName());
        landlord.setAddress(dto.getAddress());
        landlord.setPhone(dto.getPhone());
        landlord.setAvatar(dto.getAvatar() != null ? dto.getAvatar() : "/images/default-avatar.png");
        landlord.setApproved(dto.isApproved());
        return landlord;
    }

    @Override
    public List<LandlordDTO> getAllLandlords() {
        return landlordRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public LandlordDTO getLandlordById(Long id) {
        return landlordRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public LandlordDTO createLandlord(LandlordDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Landlord landlord = toEntity(dto, user);
        landlord.setId(user.getId());
        return toDTO(landlordRepository.save(landlord));
    }

    @Override
    public LandlordDTO updateLandlord(Long id, LandlordDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Landlord landlord = toEntity(dto, user);
        landlord.setId(id);
        return toDTO(landlordRepository.save(landlord));
    }

    @Override
    public void deleteLandlord(Long id) {
        landlordRepository.deleteById(id);
    }
}
