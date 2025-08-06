package com.codegym.service.impl;

import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.HouseRenter;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.HouseRenterRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseRenterService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseRenterServiceImpl implements HouseRenterService {

    @Autowired
    private HouseRenterRepository houseRenterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<HouseRenterDTO> getAllHouseRenters() {
        return houseRenterRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseRenterDTO getHouseRenterById(Long id) {
        return houseRenterRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + id));
    }

    @Override
    @Transactional
    public HouseRenterDTO createHouseRenter(HouseRenterDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + dto.getId() + " để đăng ký làm chủ nhà."));

        if (houseRenterRepository.existsById(dto.getId())) {
            throw new IllegalArgumentException("Người dùng với ID " + dto.getId() + " đã được đăng ký làm chủ nhà.");
        }

        HouseRenter houseRenter = new HouseRenter();
        houseRenter.setId(user.getId()); // ID của HouseRenter phải trùng với ID của User
        updateEntityFromDTO(houseRenter, dto, user);

        HouseRenter savedHouseRenter = houseRenterRepository.save(houseRenter);
        return toDTO(savedHouseRenter);
    }

    @Override
    @Transactional
    public HouseRenterDTO updateHouseRenter(Long id, HouseRenterDTO dto) {
        HouseRenter existingHouseRenter = houseRenterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + id + " để cập nhật."));

        updateEntityFromDTO(existingHouseRenter, dto, existingHouseRenter.getUser());

        HouseRenter updatedHouseRenter = houseRenterRepository.save(existingHouseRenter);
        return toDTO(updatedHouseRenter);
    }

    @Override
    @Transactional
    public void deleteHouseRenter(Long id) {
        if (!houseRenterRepository.existsById(id)) {
            throw new EntityNotFoundException("Không thể xóa. Chủ nhà với ID: " + id + " không tồn tại.");
        }
        houseRenterRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void lockHouseRenter(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + id + " để khóa."));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlockHouseRenter(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + id + " để mở khóa."));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getHouseRenterHouses(Long id) {
        if (!houseRenterRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + id);
        }

        List<House> houses = houseRepository.findByHouseRenterId(id);

        return houses.stream().map(this::toHouseDTO).collect(Collectors.toList());
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
}